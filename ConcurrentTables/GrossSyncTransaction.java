import java.util.ArrayList;
import java.util.List;


public class GrossSyncTransaction extends Transaction{

	public GrossSyncTransaction(int numTx, long txTime, float writeFactor) {
		super(numTx, txTime, writeFactor);
	}

	@Override
	protected void execute(long txTime, float wfactor) {
		synchronized (TableSTM.table) {
			int written = (int)(txSize*wfactor); 				//tratar fator maior que 1 e menor que 0
			int read = txSize - written;
			List<Double> reads = new ArrayList<Double>(read);
			for (int i = 0; i < read; i++) {
				reads.add(TableSTM.table[TableSTM.generateKey()]);
			}

			try {
				Thread.sleep(txTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			double sum = getSum(reads);
			for (int i = 0; i < written; i++) {
				TableSTM.table[TableSTM.generateKey()] = sum;
			}	
		}
	}

}
