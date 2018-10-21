import java.util.*;
import java.io.*;

public class BlockChain {

    private ArrayList<Block> chain = new ArrayList<Block>();
    public static void main(String args[]){
        Transaction sampleTransaction = new Transaction("hones", "lanes", 50);
        Block sampleBlock = new Block(0, sampleTransaction, "00000");
        BlockChain blockchain = new BlockChain();
    }

    public BlockChain(){
    }

    public static BlockChain fromFile(String fileName){
        BlockChain blockchain = new BlockChain();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileName));
            String line;
            int count = 0;
            long time = 0;
            String sender = "";
            String receiver = "";
            int amount = 0;
            String previousHash = "00000";
            String nonce = "";
            int index = 0;
            String hash = "";
            while ((line = br.readLine()) != null) {
                switch (count) {
                    case 0: index = Integer.parseInt(line);
                            break;
                    case 1: time = Long.parseLong(line);
                            break;
                    case 2: sender = line;
                            break;
                    case 3: receiver = line;
                            break;
                    case 4: amount = Integer.parseInt(line);
                            break;
                    case 5: nonce = line;
                            break;
                    case 6: hash = line;
                            Transaction transaction = new Transaction(sender, receiver, amount);
                            Block block = new Block(index, transaction, previousHash, time, nonce, hash);
                            previousHash = hash;
							blockchain.chain.add(block);
                            count = -1;
                            break;
                }           
                count++;
            }
        } catch (IOException e) {
			e.printStackTrace();
        } finally {
            try {
	            if (br != null) {
                	br.close();
                }
	    	} catch (IOException ex) {
				ex.printStackTrace();
            }
        }
        return blockchain;
	}
	
	public void toFile (String fileName) {
		try {
            File fout = new File(fileName);
	        FileOutputStream fos = new FileOutputStream(fout);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
            for (int i = 0; i < BlockChain.blockchain.size(); i++){
				Block block = BlockChain.blockchain.get(i);
                Transaction transaction = block.getTransaction();
                writer.write(block.getIndex());
				writer.newLine();
				writer.write(block.getTimeStamp());
                writer.newLine();
                writer.write(transaction.getSender());
				writer.newLine();
				writer.write(transaction.getReceiver());
				writer.newLine();
				writer.write(transaction.getAmount());
				writer.newLine();
				writer.write(block.getNonce());
                writer.newLine();
				writer.write(block.getHash());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean validateBlockchain(){
        return false;
    }
}
