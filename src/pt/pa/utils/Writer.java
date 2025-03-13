package pt.pa.utils;

import java.io.*;

public class Writer {
    private File file;
    private BufferedWriter bufferedWriter;

    public Writer(String filePath) {
        this.file = new File(filePath);
    }

    public boolean fileExists(){return file.exists();};

    public void open(){
        try{
            bufferedWriter = new BufferedWriter(new FileWriter(file));
        }catch(IOException | NullPointerException e){
            System.err.println(e.getMessage());
        }
    }
    public String write(String text)throws IOException{
        try {
            bufferedWriter.write(text);
        } catch (IOException e) {
            throw new IOException(e);
        }
        return file.getAbsolutePath();
    }
    public void close(){
        try{
            bufferedWriter.close();
        }catch(IOException | NullPointerException e){
            System.err.println(e.getMessage());
        }
    }
    public void write(String absolutPath, String toWrite)throws IOException{
       file = new File(absolutPath);
        open();
        write(toWrite);
        close();
    }

}