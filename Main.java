import java.util.*;
import java.util.concurrent.*;

class FileTask implements Callable<Integer> {
    private List<String> lines;

    public FileTask(List<String> lines) {
        this.lines = lines;
    }

    public Integer call() {
        int wordCount = 0;
        for (String line : lines) {
            String[] words = line.trim().split("\\s+");
            wordCount += words.length;
        }
        return wordCount;
    }
}

public class Main {
    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        List<String> fileData = Arrays.asList(
            "Java is powerful",
            "Parallel processing improves speed",
            "Multithreading is useful",
            "This is an advanced level project",
            "We are using ExecutorService in Java",
            "This project demonstrates parallel computing"
        );

        System.out.print("Enter number of threads: ");
        int numThreads = sc.nextInt();

        long startSeq = System.currentTimeMillis();

        int seqWordCount = 0;
        for (String line : fileData) {
            seqWordCount += line.split("\\s+").length;
        }

        long endSeq = System.currentTimeMillis();

        long startPar = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<Integer>> results = new ArrayList<>();

        int chunkSize = fileData.size() / numThreads;

        for (int i = 0; i < numThreads; i++) {
            int start = i * chunkSize;
            int end = (i == numThreads - 1) ? fileData.size() : start + chunkSize;

            List<String> subList = fileData.subList(start, end);
            results.add(executor.submit(new FileTask(subList)));
        }

        int parallelWordCount = 0;
        for (Future<Integer> f : results) {
            parallelWordCount += f.get();
        }

        executor.shutdown();

        long endPar = System.currentTimeMillis();

        System.out.println("\n===== RESULT =====");
        System.out.println("Total Lines: " + fileData.size());
        System.out.println("Sequential Word Count: " + seqWordCount);
        System.out.println("Parallel Word Count: " + parallelWordCount);

        System.out.println("\n===== PERFORMANCE =====");
        System.out.println("Sequential Time: " + (endSeq - startSeq) + " ms");
        System.out.println("Parallel Time: " + (endPar - startPar) + " ms");
    }
}
