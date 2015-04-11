import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Anagramatiator {

	static HashSet<Pair> anagrams = new HashSet<>();
	static List<Future<Pair>> results;
	static List<Callable<Pair>> tasks = new ArrayList<>();
	static List<String> words = new ArrayList<>();
	static ExecutorService threadPool = Executors.newFixedThreadPool(10);

	static class Pair implements Comparable<Pair> {
		private String first;
		private String second;

		public Pair(String first, String second) {
			this.first = first;
			this.second = second;
		}

		public String getFirst() {
			return first;
		}

		public String getSecond() {
			return second;
		}

		@Override
		public boolean equals(Object otherPair) {
			return (first.equals(((Pair) otherPair).getSecond()) && ((Pair) otherPair).getFirst().equals(second)) || first.equals(((Pair) otherPair).getFirst());
		}

		@Override
		public int compareTo(Pair o) {
			return (first + second).compareTo((o.getSecond() + o.getFirst()));
		}

		@Override
		public int hashCode() {
			return first.hashCode() + second.hashCode();
		}
	}

	public static void parseWords(String path) {
		try {
			String word;
			InputStream fromFile = new FileInputStream(path);
			InputStreamReader fromText = new InputStreamReader(fromFile, Charset.forName("UTF-8"));
			BufferedReader fromLine = new BufferedReader(fromText);

			while ((word = fromLine.readLine()) != null) {
				words.add(word);
			}

			fromLine.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void findAnagrammatics() {
		for (String word : words)
			tasks.add(new Callable<Pair>() {

				@Override
				public Pair call() {
					for (String otherWord : words) {

						if (!word.equals(otherWord)) {

							char[] wordChars = word.toCharArray();
							Arrays.sort(wordChars);

							char[] otherWordChars = otherWord.toCharArray();
							Arrays.sort(otherWordChars);

							if (Arrays.equals(otherWordChars, wordChars))
								return new Pair(word, otherWord);
						}
					}
					return null;
				}
			});
	}

	public static void main(String[] args) {

		parseWords("unixdict.txt");

		findAnagrammatics();

		try {
			results = threadPool.invokeAll(tasks);

			for (Future<Pair> anagramResult : results) {
				Pair anagramPair = anagramResult.get();
				if (anagramPair != null)
					anagrams.add(anagramPair);
			}

			ArrayList<Pair> pairs = new ArrayList<>(anagrams);

			pairs.sort((s1, s2) -> Integer.compare(s1.getFirst().length() + s1.getSecond().length(), s2.getFirst().length() + s2.getSecond().length()));

			for (Pair anagram : pairs)
				System.out.println(anagram.getFirst() + " " + anagram.getSecond());

			System.out.println("\n----------------------");
			System.out.println("Total " + pairs.size() + " anagrammatics found!");

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

	}

}
