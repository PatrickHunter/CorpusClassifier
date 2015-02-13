package hunter.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.*;


public class CorpusClassifier {

    /**
     * Emitts parts of speech based on how often they occur in
     * the input. for each word in the input that is a certain part
     * of speech <partofspeech, 1> will be emitted.  When a word in
     * the input maps to multiple parts of speech multiple key value
     * pairs will be emitted.
     */
    public static class PartsOfSpeechMapper  extends Mapper<Object, Text, Text, IntWritable> {
        private HashMap<String, List<String>> partsOfSpeech;
        private Path[] localFiles;
        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        /**
         * Initializes partsOfSpeech and populates it
         * using distributed cache data.
         * @param context the mapper context
         */
        @Override
        public void setup(Context context) throws IOException,
                InterruptedException{
            Scanner sc;

            try {
                localFiles = context.getLocalCacheFiles();
                sc = new Scanner(new File(localFiles[0].toUri()));
            } catch (IOException e) {
                System.out.println("IOException");
                System.out.println(e.getMessage());
                System.out.println(localFiles[0]);
                return;
            }
            partsOfSpeech = new HashMap<String, List<String>>();
            while (sc.hasNext()) {
                String key = sc.next();
                if (sc.hasNext()) {
                    if (partsOfSpeech.containsKey(key)) {
                        List<String> parts = partsOfSpeech.get(key);
                        parts.add(sc.next());
                    }else{
                        List<String> parts = new ArrayList<String>();
                        parts.add(sc.next());
                        partsOfSpeech.put(key,parts);
                    }
                }
            }
        }

        /**
         * @param key     not actually used, required by mapreduce api.
         * @param value   input text from the corpus being analyzed
         * @param context the job context
         * @throws IOException
         * @throws InterruptedException Classifies every word in value the emits the relevant part of speech and a counter.
         *                              If a word maps to multiple parts of speech this is done for each one.
         */
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            StringTokenizer itr = new StringTokenizer(value.toString());
            while (itr.hasMoreTokens()) {
                String hashKey = itr.nextToken();
                if(partsOfSpeech.containsKey(hashKey)) {
                    List<String> parts = partsOfSpeech.get(hashKey);
                    for (String part : parts) {
                        word.set(part);
                        context.write(word, one);
                    }
                }
            }
        }


    }

    public static class IntSumReducer
            extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values,
                           Context context
        ) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }


    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "corpus classifier");
        job.setJarByClass(CorpusClassifier.class);
        job.setMapperClass(PartsOfSpeechMapper.class);
        job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        job.addCacheFile(new URI(args[2]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
