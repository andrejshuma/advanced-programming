package labs.lab4.zad1;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;


class TermFrequency{
	String[]stopWords;
	List<String>words;
	Map<String,Integer>wordCount;

	public TermFrequency(InputStream inputStream,String[]stopWords){
		this.stopWords=stopWords;
		wordCount=new HashMap<>();
		BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
		List<String>justWords=reader.lines()
				.flatMap(line-> Arrays.stream(line.toLowerCase()
								.replaceAll("\\.","")
								.replaceAll(",","")

								.split("\\s+")
						)

				)
				.collect(Collectors.toList());
		words=justWords.stream().filter(w-> !w.isEmpty()&&!Arrays.stream(stopWords).collect(Collectors.toList()).contains(w)).collect(Collectors.toList());

	}

	public int countTotal(){
		return words.size();
	}
	public int countDistinct(){
		return (int) words.stream().distinct().count();
	}

	public List<String>mostOften(int k){
		words.forEach(w->wordCount.merge(w,1,Integer::sum));
		return wordCount.entrySet().stream()
				.sorted(Map.Entry.<String,Integer>comparingByValue().reversed()
						.thenComparing(Map.Entry::getKey))
				.limit(k)
				.map(Map.Entry::getKey)
				.collect(Collectors.toList());


	}



}


public class TermFrequencyTest {
	public static void main(String[] args) throws FileNotFoundException {
		String[] stop = new String[] { "во", "и", "се", "за", "ќе", "да", "од",
				"ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
				"што", "на", "а", "но", "кој", "ја" };
		TermFrequency tf = new TermFrequency(System.in,
				stop);
		System.out.println(tf.countTotal());
		System.out.println(tf.countDistinct());
		System.out.println(tf.mostOften(10));
	}
}