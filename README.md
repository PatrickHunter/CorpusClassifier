# CorpusClassifier
Takes a text and a many to many mapping from keywords to classification, then counts the frequency of words from each classification.

This is part of a project that builds off the Apache Hadoop tutorial http://hadoop.apache.org/docs/current/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html  the rest of the project is available at https://github.com/PatrickHunter/dictionary-mangler
I built this to solidify what I learned and to learn Hadoop features not used in the tutorial’s WordCount example e.g. the distributed cache.  Others who have just finished the Apache tutorial may find this project to be a useful example.
The command line input format is: input folder output folder classifications file with all inputs in the form of hdfs locations.  Please note that the first two arguments are folders and the third argument is a file.  The input folder can contain any text file(s) that you want analyzed.  The classifications file should have keys and values on alternating lines and should start with a key.  Excess whitespace will be ignored, additionally; keys and values can both be repeated.  For example, the following would be valid content for a classifications file.


AAM
	a.

AAM
	n.

AB
	n.

ABACA
	a.

My other repository https://github.com/PatrickHunter/dictionary-mangler contains code that I used to create a classification file that maps words to parts of speech.  As final note, this program worked as decribed when I ran it, but its purpose was to teach me Hadoop, and I make no guarantees as to its fitness for any other purpose.
