// importing stuff
import java.util.*;
import java.io.*;
public class BlockChain {

    // chain arraylist for blockchain
    private ArrayList<Block> chain = new ArrayList<Block>();
    
    // main method
    public static void main(String args[]){
        // scanner for user input
        Scanner scanner = new Scanner(System.in);

        // read in blockchain from user specified txt file
        System.out.println("\nPlease enter the blockchain txt file you want to read in\n");
        BlockChain blockchain = fromFile(scanner.next());
        
        // check if input file is valid
        if (blockchain.validateBlockchain()){

            // start asking user for new transaction
            System.out.println("\nWould you like to make a new transaction? Enter 'yes' or 'no'.\n");

            // while user enters "yes" keep asking if they want a new transaction
            while (scanner.next().equals("yes")) {

                // ask user for transaction info
                System.out.println("\nPlease enter the sender:\n");
                String username = scanner.next();
                System.out.println("\nPlease enter the reciever:\n");
                String reciever = scanner.next();
                System.out.println("\nPlease enter the amount for the transaction:\n");
                int amount = scanner.nextInt();

                // check if sender has required balance
                if (blockchain.getBalance(username) >= amount) {

                    // create the transaction and block, and add to chain
                    Transaction newTransaction = new Transaction(username, reciever, amount);
                    Block newBlock = new Block(blockchain.chain.size(), newTransaction, blockchain.chain.get(blockchain.chain.size()-1).getHash());
                    blockchain.add(newBlock);
                    System.out.println("\nTransaction successfully added!");
                } else {
                    // if not enough funds
                    System.out.println("\nSender does not have adequate funds!");
                }
                System.out.println("\nWould you like to make a new transaction? Enter 'yes' or 'no'.\n");
            }
            System.out.println("\nGoodbye!");
        }

        // at the end, write the curent blockchain to a new file
        blockchain.toFile("300010911_blockchain.txt");
    }

    // empty constructor
    public BlockChain(){
    }

    // from file function, reads in blockchain txt file
    public static BlockChain fromFile(String fileName){
        // instantiate new blockchain object
        BlockChain blockchain = new BlockChain();
        BufferedReader br = null;
        try {
            // set all the required varibles to create a new transaction and block
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

            // read in the file line by line, since the format/order is always maintained a switch a 
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
                            // create the new objects, add it to block chain
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
        // return created blockchain
        return blockchain;
	}
    
    // writing to a file
	public void toFile (String fileName) {
		try {
            File fout = new File(fileName);
	        FileOutputStream fos = new FileOutputStream(fout);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
            for (int i = 0; i < this.chain.size(); i++){
                // loop through chain and print out stuff in correct order
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

    // validate the block chain by traversing the linked list and checking the hashes
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

    // add a block to the chain
    public void add(Block block){
        this.chain.add(block);
    }

    // get the balance of the user through traversing through the linked list and checking username
    // would use a hashmap to track it to save time, but not sure if we are allowed to use that yet
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
