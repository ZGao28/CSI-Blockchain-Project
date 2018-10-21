
import java.util.*;
import java.io.*;

public class BlockChain {

    private ArrayList<Block> chain = new ArrayList<Block>();
    public static void main(String args[]){
        Scanner scanner = new Scanner(System.in);
        Transaction sampleTransaction = new Transaction("hones", "lanes", 50);
        Block sampleBlock = new Block(0, sampleTransaction, "00000");
        BlockChain blockchain = fromFile("sampleblockchain.txt");
        
        if (blockchain.validateBlockchain()){
            System.out.println("\nWould you like to make a new transaction? Enter 'yes' or 'no'.\n");
            while (scanner.next().equals("yes")) {
                System.out.println("\nPlease enter the sender:\n");
                String username = scanner.next();
                System.out.println("\nPlease enter the reciever:\n");
                String reciever = scanner.next();
                System.out.println("\nPlease enter the amount for the transaction:\n");
                int amount = scanner.nextInt();
                if (blockchain.getBalance(username) >= amount) {
                    Transaction newTransaction = new Transaction(username, reciever, amount);
                    Block newBlock = new Block(blockchain.chain.size(), newTransaction, blockchain.chain.get(blockchain.chain.size()-1).getHash());
                    blockchain.add(newBlock);
                    System.out.println("\nTransaction successfully added!");
                } else {
                    System.out.println("\nSender does not have adequate funds!");
                }
                System.out.println("\nWould you like to make a new transaction? Enter 'yes' or 'no'.\n");
            }
            System.out.println("\nGoodbye!");
        }
        blockchain.toFile("300010911_blockchain.txt");
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
            for (int i = 0; i < this.chain.size(); i++){
				Block block = this.chain.get(i);
                Transaction transaction = block.getTransaction();
                writer.write(block.getIndex());
				writer.newLine();
				writer.write(block.getTimeStamp());
                writer.newLine();
                writer.write(transaction.getSender());
				writer.newLine();
				writer.write(transaction.getReceiver());
				writer.newLine();
				writer.write(Integer.toString(transaction.getAmount()));
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
        for (int i = 0; i < this.chain.size(); i++){
            Block block = this.chain.get(i);
            try {
                if (!Sha1.hash(block.toString()).equals(block.getHash())){
                    return false;
                }
            } catch(UnsupportedEncodingException e){
                e.printStackTrace();
            }
        }
        return true;
    }

    public void add(Block block){
        this.chain.add(block);
    }

    public int getBalance(String username){
        int count = 0;
        for (int i = 0; i < this.chain.size(); i++){
            Block block = this.chain.get(i);
            Transaction transaction = block.getTransaction();
            
            if (transaction.getSender().equals(username)){
                count -= transaction.getAmount();
            }

            if (transaction.getReceiver().equals(username)){
                count += transaction.getAmount();
            }
        }
        return count;
    }
}
