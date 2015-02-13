# CorpusClassifier
Takes a text and a many to many mapping from keywords to classification, the counts the frequency of words from each classification.

This is part of a project that build off of the apache hadoop tutorial http://hadoop.apache.org/docs/current/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html 
I build it to soldify my knowegle of what the tutorial covers and to get expreince with hadoop features not used in their WordCount example
e.g. the distributed cache.
The command line input format is input folder output folder classifications file with all inputs in the form of hdfs locations. 
Please note that the first two are folders and the third is a file.  The input folder can contain any text file(s) that you want analyzed.
The classification folder should have keys and values on alternating lines starting with a key.  Keys and values can both be reapted, for example:

AAM
	a.

AAM
	n.

AB
	n.

ABACA
	a.

My other repository https://github.com/PatrickHunter/dictionary-mangler contians code that I used to create an classification file
mapping words to parts of speech.  I would like to reiterate that while this program worked well for me its puprose was to teach me
hadoop, and I make no gaurantees as to its fittness for anyother purpose.
