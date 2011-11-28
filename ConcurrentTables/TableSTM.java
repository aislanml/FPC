import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class TableSTM {

	public static double[] table;
			
	private static int capacity;
	private static int number0fThreads;
	private static int numberOfTransactions;
	private static long txTime;
	private static float writeFactor;
	private final static String FILE_EXTENSION = ".out";
	
	public static void main(String[] args) throws InterruptedException, IOException{

		capacity = Integer.parseInt(args[0]);
		number0fThreads = Integer.parseInt(args[1]);
		numberOfTransactions = Integer.parseInt(args[2]);
		txTime = Long.parseLong(args[3]);
		writeFactor = Float.parseFloat(args[4]);
		int iterations = Integer.parseInt(args[5]);
		boolean optimistMode = Integer.parseInt(args[6]) == 1 ?true:false;		// 0 = abordagem grossa ; 1 = abordagem otimista;
		
		
		
		List<Thread> transactions;
		List<Long> executions = new ArrayList<Long>();
		for (int i = 0; i < iterations; i++) {
			transactions = createTransactions(number0fThreads, numberOfTransactions, txTime, writeFactor, optimistMode);
			executions.add(getExcution(transactions));
		}
		
		String mode = optimistMode? "STM" : "Gross";
		save(getFileName(mode), executions);
		
	}

	
	private static double[] populate(int capacity){
		double[] array = new double[capacity];
		for (int i = 0; i < capacity; i++) {
			array[i] = i;
		}
		return array;
	
	}
	
	private static List<Thread> createTransactions(int number0fThread, int numTx,long txTime,float wFactor, boolean optMode){
		List<Thread> workers = new ArrayList<Thread>(number0fThread);
		if(optMode){
			for (int i = 0; i < number0fThread; i++) {
				workers.add(new Thread(new AtomicTransaction(numTx, txTime, wFactor)));
			}
		}else{
			for (int i = 0; i < number0fThread; i++) {
				workers.add(new Thread(new GrossSyncTransaction(numTx, txTime, wFactor)));
			}
		}
		
		return workers;
	}
	
	public static int generateKey(){
		
		return (int) (Math.random()*table.length);
		
	}
	
	private static void startWorkers(List<Thread> workers){
		for (Thread t : workers) {
			t.start();
		}
	}
	
	private static void joinWorkes(List<Thread> workers) throws InterruptedException{
		for (Thread t : workers) {
			t.join();
		}
	}
	
	public static void print() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < table.length; i++) {
		
			sb.append("\n" + table[i]);
		}
		
		System.out.println(sb.toString().trim());
	}
	
	public static double sum(){
		double sum = 0;
		for (int i = 0; i < table.length; i++) {
			sum += table[i]; 
		}
		return sum;
	}
	
	
	private static long getExcution(List<Thread> transactions) throws InterruptedException{

		table = populate(capacity);
		
		long startTime = System.nanoTime();
		startWorkers(transactions);
		joinWorkes(transactions);
		
		return System.nanoTime() - startTime;
	}
	
	
	private static String getFileName(String mode){
		String sep = "_"; 
		return new String(mode +capacity +sep+ number0fThreads +sep+ numberOfTransactions +sep+ txTime +sep+ writeFactor + FILE_EXTENSION);
	}


	private static void save(String fileName, List<Long> executions) throws IOException{
		File file = new File(fileName);
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		String parameters = new String(capacity +" "+ number0fThreads +" "+ numberOfTransactions +" "+ txTime +" "+ writeFactor);
		fileOutputStream.write((parameters + "\n").getBytes());
		for (Long l : executions) {
			fileOutputStream.write((l + "\n").getBytes());
		}

		fileOutputStream.close();
	}
	
}
