import org.deuce.Atomic;


public class Trans extends Transaction{
	
	
	public Trans(int numTx, long txTime, float writeFactor) {
		super(numTx, txTime, writeFactor);
	}

	@Atomic
	protected void execute(long txTime, float wfactor){
		for (int i = 0; i < 1000; i++) {
			TableSTM.table[TableSTM.generateKey()]++;
		}
	}


}
