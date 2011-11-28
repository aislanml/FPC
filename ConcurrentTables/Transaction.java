import java.util.List;


public abstract class Transaction implements Runnable {

	protected int numTx;
	protected long txTime;
	protected float writeFactor;
	protected final static int txSize = 10;
	
	public Transaction(int numTx, long txTime,float writeFactor) {
		this.numTx = numTx;
		this.txTime = txTime;
		this.writeFactor = writeFactor;
	}
	
	@Override
	public void run() {
		for (int i = 0; i < numTx; i++) {
			execute(txTime, writeFactor);
		}
		
	}
	
	protected abstract void execute(long txTime, float wfactor);
	
	protected double getSum(List<Double> list){
		double sum = 0;
	
		for (Double d : list) {
			sum += d;
		}
		
		return sum;
	}

}
