import java.sql.Timestamp;

public class Block {
    private int index;
    // the index of the block in the list
    private java.sql.Timestamp timestamp; // time at which transaction
    // has been processed
    private Transaction transaction; // the transaction object
    private String nonce;
    // random string (for proof of work)
    private String previousHash;
    // previous hash (set to "" in first block)
    //(in first block, set to string of zeroes of size of complexity "00000")
    private String hash; // hash of the block (hash of string obtained from previous variables via toString() method)

    public Block(int index, Transaction transaction, String previousHash){
        this.index = index;
        this.transaction = transaction;
        this.previousHash = previousHash;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.hash = this.calculateHash();
    }

    public Block(int index, Transaction transaction, String previousHash, long time, String nonce, String hash){
        this.index = index;
        this.transaction = transaction;
        this.previousHash = previousHash;
        this.timestamp = new Timestamp(time);
        this.nonce = nonce;
        this.hash = hash;
    }

    private String calculateHash(){
        return "";
    }

    public String toString() {
        return timestamp.toString() + ":" + transaction.toString() + "." + nonce + previousHash;
    }

    public String getIndex(){
        return Integer.toString(this.index);
    }

    public Transaction getTransaction(){
        return this.transaction;
    }

    public String getPreviousHash(){
        return this.previousHash;
    }

    public String getNonce(){
        return this.nonce;
    }

    public String getTimeStamp(){
        return Long.toString(this.timestamp.getTime());
    }

    public String getHash(){
        return this.hash;
    }
}