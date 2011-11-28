import java.util.ArrayList;
import java.util.List;

import org.deuce.Atomic;


public class AtomicTransaction extends Transaction {

	
	public AtomicTransaction(int numTx, long txTime, float writeFactor) {
		super(numTx, txTime, writeFactor);
	}

	@Atomic
	protected void execute(long txTime, float wfactor){
		int written = (int)(txSize*wfactor); 				//tratar fator maior que 1 e menor que 0
		int read = txSize - written;
		List<Double> reads = new ArrayList<Double>(read);
		for (int i = 0; i < read; i++) {
			reads.add(TableSTM.table[TableSTM.generateKey()]);
		}

		try {
			Thread.sleep(txTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		double sum = getSum(reads);
		for (int i = 0; i < written; i++) {
			TableSTM.table[TableSTM.generateKey()] = sum;
		}
		
	}
	
}
