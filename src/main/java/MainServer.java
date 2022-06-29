import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import org.python.apache.commons.compress.utils.IOUtils;
import org.python.util.PythonInterpreter;

import javax.imageio.ImageIO;
import javax.swing.*;

public class MainServer implements Runnable{
    static final File WEB_ROOT = new File(".");
    static final String DEFAULT_FILE = "index.py";
    static final String FILE_NOT_FOUND = "404.html";
    static final String METHOD_NOT_SUPPORTED = "not_supported.html";

    ArrayList<String> MimeTypes = new ArrayList<>();
    //DataInputStream UploadFileStream;
    byte[] UploadFileData;
    int Length;
    //InputStream SocketDataStream;
    ByteArrayOutputStream SocketOutputStream;
    byte[] MainDataBytes;

    private Socket connect;

    static final int PORT = 3335;
    static final boolean admin = true;

    public MainServer(Socket c) {
        this.connect = c;
    }

    public static void main(String[] args) {
        try {
            ServerSocket serverConnect = new ServerSocket(PORT);
            System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");

            // we listen until user halts server execution
            while (true) {
                MainServer myServer = new MainServer(serverConnect.accept());

                if (admin) {
                    System.out.println("New connecton opened on: (" + new Date() + ")");
                }

                // create dedicated thread to manage the client connection
                Thread thread = new Thread((Runnable) myServer);
                System.out.println("Thread Opened with id : " + thread.getId());
                if (thread.getId() == 15)
                {
                    File textFile = new File(System.getProperty("user.dir"), "Calculator.txt");

                    try {
                        FileWriter output = new FileWriter(textFile);
                        output.write("");
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                thread.start();
            }

        } catch (IOException e) {
            System.err.println("Server Connection error : " + e.getMessage());
        }
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        BufferedOutputStream dataOut = null;
        String fileRequested = null;

        try {

            MimeTypes.add("png");
            MimeTypes.add("jpg");
            MimeTypes.add("jpeg");

            InputStream socketDataStream = connect.getInputStream();
            //SocketOutputStream = new ByteArrayOutputStream(); // to make copies


            /*byte[] buffer = new byte[1024];
            int len;
            while ((len = socketDataStream.read(buffer)) > -1) {
                SocketOutputStream.write(buffer, 0, len);
            }
            SocketOutputStream.flush();


            int len = dis.readInt();
            System.out.println("Image Size: " + len/1024 + "KB");

            byte[] data = new byte[len]; //UploadFileData
            dis.readFully(data);
            dis.close();*/

            System.out.println("CHecking");

            /*if (socketDataStream.markSupported()) {
                System.out.println("Input read");
                socketDataStream.mark(Integer.MAX_VALUE);
                MainDataBytes = socketDataStream.readAllBytes();
                socketDataStream.reset();
            }


            InputStream mainInput = new ByteArrayInputStream(MainDataBytes);*/

            System.out.println("COPIES MADE");

            in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            out = new PrintWriter(connect.getOutputStream());
            dataOut = new BufferedOutputStream(connect.getOutputStream());
            //UploadFileStream = new DataInputStream(new BufferedInputStream(connect.getInputStream()));

            String streamData = "";

            //DataInputStream UploadFileStream = new DataInputStream(connect.getInputStream());


            //Length = UploadFileStream.readInt();
            //System.out.println("Image Size: " + Length/1024 + "KB");
            //UploadFileStream.reset();








            // get first line of the request from the client
            String input = in.readLine();
            int lineCount = input.length();
            streamData = input;

            StringTokenizer parse = new StringTokenizer(input);
            String parser[] = new String[parse.countTokens()];

            String method = "";//parse.nextToken().toUpperCase();
            fileRequested = "";//parse.nextToken().toLowerCase();

            System.out.println("Server Info : First Line: ");

            for(int i=0; i < parser.length; i++ ){ //first input
                String token = parse.nextToken();
                //System.out.println(token);
                parser[i] = token;
                if(i ==0)
                    method = token;
                if(i ==1)
                    fileRequested = token;
            }


            /*code to read and print headers
            String headerLine = null;
            while((headerLine = br.readLine()).length() != 0){
                System.out.println(headerLine);
            }*/

            /*code to read the post payload data
            System.out.println("Server Info 2 : ");
            System.out.println();
            StringBuilder payload = new StringBuilder();
            while(in.ready()){
                char value = (char) in.read();
                payload.append(value);
                System.out.print(value);
            }
            System.out.println();
            System.out.println("Payload data is: "+payload.toString());*/


            //get more lines of the request from the client
            System.out.println("Server Info 2 : Rest: ");
            //input = in.readLine();
            //System.out.println(input);


            while ((input = in.readLine()).isEmpty() ==false) {
                System.out.print("");
                lineCount = lineCount + input.length();
                streamData = streamData + input;
            }
            streamData = streamData + input;


            //String method = parse.nextToken().toUpperCase();
            //fileRequested = parse.nextToken().toLowerCase();
            //in.mark(lineCount);

            StringBuilder postPayload = null;
            if(in.ready()) { //MORE (deal with Post)
                //System.out.println("found more info...");

                postPayload = new StringBuilder();
                while (in.ready()) {
                    char value = (char) in.read();
                    postPayload.append(value);
                    streamData = streamData + String.valueOf(value);
                    //System.out.print(value);
                }
                System.out.println();
                System.out.println("Payload data is: ");
                System.out.println(postPayload.toString());


                /*System.out.println("FILE INPUT FROM SERVER SHOULD BE HERE!!!!!");

                StringBuffer inputLine = new StringBuffer();
                String tmp;

                    while ((tmp = inFile.readUTF()) != null) {
                        inputLine.append(tmp);
                        System.out.println(tmp);
                    }
                    inFile.close();*/
            }

            //in.reset();

            MainDataBytes = streamData.getBytes(StandardCharsets.UTF_8);


            System.out.println();
            System.out.println("Method just sent is : " + method);
            System.out.println("fileRequested just sent is : " + fileRequested);

            // we support only GET and HEAD methods, we check
            if(method.equals("GET") || method.equals("HEAD") ){

                if (fileRequested.endsWith("/")) {
                    fileRequested += DEFAULT_FILE;
                }
                String contentMimeType = getContentType(fileRequested);

                //Save to textfile
                String processfile = setTextFile(fileRequested);
                //checkPages(fileRequested);


                if (method.equals("GET")) { // GET method so we return conten

                    if(contentMimeType.equals("text/html")) { // normal html
                        File file = new File(WEB_ROOT, fileRequested);

                        //byte[] fileData = readFileData(file, fileLength);
                        //Run CGI FILE
                        String htmlOutput ="";
                        if(!processfile.equals(""))
                            htmlOutput = processFile(processfile); //process.toString();
                        else
                            htmlOutput = processFile(DEFAULT_FILE);

                        File htmlFile = new File(System.getProperty("user.dir"), "output.html");
                        FileWriter output = new FileWriter(htmlFile);
                        output.write(htmlOutput);
                        output.close();

                        int htmlFileLength = (int) htmlFile.length();
                        byte[] fileData = readFileData(htmlFile, htmlFileLength);

                        // send HTTP Headers
                        out.println("HTTP/1.1 200 OK");
                        out.println("Server: HTTPServer by u18003517 : 1.0");
                        out.println("Date: " + new Date());
                        out.println("Content-type: " + contentMimeType);
                        out.println("Content-length: " + htmlFileLength);
                        out.println(); // blank line between headers and content, very important !
                        out.flush();

                        dataOut.write(fileData, 0, htmlFileLength);
                        dataOut.flush();
                    }
                    else{ //show images

                        while(fileRequested.charAt(0) == '/')
                            fileRequested = fileRequested.substring(1, fileRequested.length());

                        File fileI = new File(fileRequested);
                        System.out.println(fileI);
                        int FileLength = (int) fileI.length();
                        System.out.println(fileI.length());
                        byte[] fileData = readFileData(fileI, FileLength);


                        // send HTTP Headers
                        out.println("HTTP/1.1 200 OK");
                        out.println("Server: HTTPServer by u18003517 : 1.0");
                        out.println("Date: " + new Date());
                        out.println("Content-type: " + contentMimeType);
                        out.println("Content-length: " + FileLength);
                        out.println(); // blank line between headers and content, very important !
                        out.flush();


                        /*
                        String extention = contentMimeType.substring(contentMimeType.indexOf("/")+1, contentMimeType.length());

                        BufferedImage bImage = ImageIO.read(fileI);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ImageIO.write(bImage, extention, bos );
                        byte [] data = bos.toByteArray();*/

                        dataOut.write(fileData, 0, FileLength);
                        //dataOut.write(data, 0, data.length);
                        dataOut.flush();
                    }
                }

                if (admin) {
                    System.out.println("File " + fileRequested + " of type " + contentMimeType + " returned");
                    System.out.println("200: Success: " + method + " method used.");
                }
            }
            else if (method.equals("POST") ){
                if (fileRequested.endsWith("/")) {
                    fileRequested += DEFAULT_FILE;
                }
                String contentMimeType = getContentType(fileRequested);//"image/png";

                //Save to textfile
                String processfile = setTextFile(fileRequested);
                //checkPages(fileRequested);



                if (method.equals("POST")) {
                    File file = new File(WEB_ROOT, fileRequested);

                    //byte[] fileData = readFileData(file, fileLength);
                    //Run CGI FILE
                    String htmlOutput ="";
                    if(!processfile.equals(""))
                        htmlOutput = processFile(processfile); //process.toString();
                    else
                        htmlOutput = processFile(processValue(fileRequested,postPayload.toString()));

                    File htmlFile = new File(System.getProperty("user.dir"), "output.html");
                    FileWriter output = new FileWriter(htmlFile);
                    output.write(htmlOutput);
                    output.close();

                    int htmlFileLength = (int) htmlFile.length();
                    byte[] fileData = readFileData(htmlFile, htmlFileLength);

                    // send HTTP Headers
                    out.println("HTTP/1.1 200 OK");
                    out.println("Server: HTTPServer by u18003517 : 1.0");
                    out.println("Date: " + new Date());
                    out.println("Content-type: " + contentMimeType);
                    out.println("Content-length: " + htmlFileLength);
                    out.println(); // blank line between headers and content, very important !
                    out.flush();


                    dataOut.write(fileData, 0, htmlFileLength);
                    dataOut.flush();
                }


                if (admin) {
                    System.out.println("File " + fileRequested + " of type " + contentMimeType + " returned");
                    System.out.println("200: Success: " + method + " method used.");
                }
            }
            else{
                String contentMimeType = "text/html";

                File file = new File(WEB_ROOT, METHOD_NOT_SUPPORTED);
                int fileLength = (int) file.length();
                byte[] fileData = readFileData(file, fileLength); //read content to return to client

                // we send HTTP Headers with data to client
                out.println("HTTP/1.1 501 Not Implemented");
                out.println("Server: HTTPServer by u18003517 : 1.0");
                out.println("Date: " + new Date());
                out.println("Content-type: " + contentMimeType);
                out.println("Content-length: " + fileLength);
                out.println(); // blank line between headers and content, very important !
                out.flush();


                dataOut.write(fileData, 0, fileLength); // file
                dataOut.flush();

                if (admin) {
                    System.out.println("501 Not Implemented : " + method + " method found.");
                    //System.out.println( fileRequested);
                }

            }

        } catch (FileNotFoundException e) {
            try {
                fileNotFound(out, dataOut, fileRequested);
            } catch (IOException ioe) {
                System.err.println("Error with file not found exception : " + ioe.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                dataOut.close();
                connect.close(); // close socket
            } catch (Exception e) {
                System.err.println("Error closing stream : " + e.getMessage());
            }

            if (admin) {
                System.out.println("Connection closed.\n");
            }
        }

    }

    private void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {

        String content = "text/html";
        File file = new File(WEB_ROOT, FILE_NOT_FOUND);
        int fileLength = (int) file.length();
        byte[] fileData = readFileData(file, fileLength);

        out.println("HTTP/1.1 404 File Not Found");
        out.println("Server: HTTPServer by u18003517 : 1.0");
        out.println("Date: " + new Date());
        out.println("Content-type: " + content);
        out.println("Content-length: " + fileLength);
        out.println(); // blank line between headers and content, very important !
        out.flush(); // flush character output stream buffer

        dataOut.write(fileData, 0, fileLength);
        dataOut.flush();

        if (admin) {
            System.out.println("File " + fileRequested + " not found");
        }
    }

    private byte[] readFileData(File file, int fileLength) throws IOException {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];

        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null)
                fileIn.close();
        }

        return fileData;
    }

    private String getContentType(String fileRequested) {
        /*if (fileRequested.endsWith(".htm")  ||  fileRequested.endsWith(".html"))
            return "text/html";
            //return "text/plain";*/

        int firstIndex = fileRequested.indexOf("/");
        int lastIndex = fileRequested.indexOf(".");
        //String Value = fileRequested.substring(firstIndex+1,lastIndex  );
        if(lastIndex == -1)
            return "text/html";

        String Extention = fileRequested.substring(lastIndex, fileRequested.length());


        if(Extention.charAt(Extention.length()-1) =='?')
            Extention = Extention.substring(0,Extention.length()-1);

        System.out.println("EXTENTION : "+Extention);

        if(Extention.equals(".png"))
            return "image/png";
        if(Extention.equals(".jpg"))
            return "image/jpg";
        if(Extention.equals(".jpeg"))
            return "image/jpeg";

        for(int i=0; i < MimeTypes.size(); i++)
            if(Extention.equals("." +MimeTypes.get(i)))
                return "image/" + MimeTypes.get(i);

        return "text/html";
    }



    private String processFile(String file){
        StringWriter out = new StringWriter();
        PythonInterpreter interpreter = new PythonInterpreter();

        interpreter.setOut(out);
        interpreter.setErr(out);
        interpreter.execfile(file);
        String result = out.toString();
        //System.out.println(result);
        return result;
    }

    private String setTextFile(String fileRequested) throws IOException {
        //System.out.println("Test Text file input : " + fileRequested);

        File textFile = new File(System.getProperty("user.dir"), "ServerData/Cellphones.txt");

        if(!textFile.exists()) {
            try {
                FileWriter output = new FileWriter(textFile);
                output.write("");
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int firstIndex = fileRequested.indexOf("/");
        int lastIndex = fileRequested.indexOf(".");

        if(lastIndex == -1) //no extention = null
            return "";

        String Value = fileRequested.substring(firstIndex+1,lastIndex  );
        String Extention = fileRequested.substring(lastIndex, fileRequested.length());



        if(fileRequested.equals("/.py?"))
            Value = "/";

        if(Extention.charAt(Extention.length()-1) =='?')
            Extention = Extention.substring(0,Extention.length()-1);

        if(!Extention.equals(".py"))
            Value = Value + "F";


        if(checkValue(Value) == false) {
            if(Value.charAt(Value.length()-1) =='F')
                Value = Value.substring(0,Value.length()-1);

            File file = new File(Value + Extention);
            int fileLength = (int) file.length();
            byte[] fileData = readFileData(file, fileLength); //checks if file exists then

            return Value + Extention;
        }



        return "";


        /*try {
            Scanner myReader = new Scanner(textFile);
            String line= "";
            while (myReader.hasNextLine()) {
                line = myReader.nextLine();
            }

            System.out.println("Line from reader : " + line);
            line = line + Value;

            FileWriter output = new FileWriter(textFile);
            output.write(line);
            output.close();

        } catch (IOException e) {
            e.printStackTrace();
        }*/



    }

    private boolean checkValue(String value) {
        if (value.equals("addNumber"))
            return true;
        if (value.equals("updateNumber"))
            return true;
        if (value.equals("updateNumberP"))
            return true;
        if (value.equals("deleteNumber"))
            return true;
        if (value.equals("LookNumbers")) {
            File textFile = new File(System.getProperty("user.dir"), "searchedPhones.txt");

            try {
                FileWriter output = new FileWriter(textFile);
                output.write("");
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        if (value.equals("searchNumber"))
            return true;

        return false;
    }


    private String processValue(String fileRequested, String postPayload){
        int firstIndex = fileRequested.indexOf("/");
        int lastIndex = fileRequested.indexOf(".");
        String value = fileRequested.substring(firstIndex+1,lastIndex  );
        /*String Extention = fileRequested.substring(lastIndex, fileRequested.length());
        if(Extention.charAt(Extention.length()-1) =='?')
            Extention = Extention.substring(0,Extention.length()-1);*/


        if (value.equals("addNumber")){
            add(postPayload);
            return "AddNumbers.py"; //TODO check which to sent back
        }
        else if (value.equals("updateNumber")) {
            File textFile = new File(System.getProperty("user.dir"), "updatePost.txt");
            try {
                FileWriter output = new FileWriter(textFile);
                output.write(postPayload);
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "updateNumber.py";
        }
        else if (value.equals("updateNumberP")) {
            update(postPayload);
            return "UpdateNumbers.py";
        }
        else if (value.equals("deleteNumber")) {
            delete(postPayload);
            return "DeleteNumbers.py";
        }
        else if (value.equals("searchNumber")) {
            search(postPayload);
            return "LookNumbers.py";
        }

        return "";
    }

    private void add(String payload) {
        File textFile = new File(System.getProperty("user.dir"), "ServerData/Cellphones.txt");
        try {
            Scanner myReader = new Scanner(textFile);
            ArrayList<String> line = new ArrayList<>();
            while (myReader.hasNextLine()) {
                line.add(myReader.nextLine());
            }

            List<Object> values = constructPayload(payload);
            //(String) tokenInfo.get(0), (int) tokenInfo.get(3)

            String name = (String) values.get(0);
            String cell = (String) values.get(1);
            String profile = (String) values.get(2);


            System.out.println("ADDING...");
            //System.out.println(name + " : " + cell);

            //TODO Picture

            String newLine = name +"," + cell + "," + profile;
            System.out.println("New Line to post : " + newLine);
            line.add(newLine);

            FileWriter output = new FileWriter(textFile);
            int index =0;
            while(index < line.size()) {
                output.write(line.get(index));
                if(index != (line.size()-1))
                    output.write("\n");
                index = index +1;
            }
            output.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void update(String payload) {
        File textFile = new File(System.getProperty("user.dir"), "ServerData/Cellphones.txt");
        try {
            Scanner myReader = new Scanner(textFile);
            ArrayList<String> line = new ArrayList<>();
            ArrayList<String> updateLine = new ArrayList<>();
            while (myReader.hasNextLine()) {
                line.add(myReader.nextLine());
            }

            File updateFile = new File(System.getProperty("user.dir"), "updatePost.txt");
            Scanner updateReader = new Scanner(updateFile);

            String name = "";
            String cell = "";

            String updateline= "";
            while (updateReader.hasNextLine()) {
                updateline = updateReader.nextLine();
                System.out.println("UPDATE DATA ........................................ " + updateline);
                if(updateline.isEmpty()){
                    System.out.println("YES");
                    updateline = updateReader.nextLine();
                    name = updateline.substring(0, updateline.indexOf("-"));
                    cell = updateline.substring(updateline.indexOf("-")+1, updateline.length());
                    break;
                }
                /*if(updateReader.hasNextLine())
                    updateline = updateline + "\n";
                System.out.println( updateline);*/
            }




            //List<Object> values = constructUpdatePayload(updateline);
            //String name = (String) values.get(0);
            //String cell = (String) values.get(1);
            String profile = "null";

            System.out.println();
            System.out.println("POST LONG AGO" + updateline);
            System.out.println(payload);
            System.out.println("UPDATING...");


            for(int i=0; i< line.size();i++){
                List<Object> linevalues = analyseLine(line.get(i));
                String tname = (String) linevalues.get(0);
                String tcell = (String) linevalues.get(1);
                String tprofile = (String) linevalues.get(2);

                System.out.println(name + " : " + cell);
                System.out.println(tname + " : " + tcell);
                if(tname.equals(name) && tcell.equals(cell)){// found
                    List<Object>  values = constructPayload(payload);
                    name = (String) values.get(0);
                    cell = (String) values.get(1);
                    profile = (String) values.get(2);

                    if(name == "null")
                        name = tname;
                    if(cell == "null")
                        cell = tcell;
                    if(profile == "null")
                        profile = tprofile;

                    //TODO Picture

                    String newLine = name +"," + cell + "," + profile;
                    updateLine.add(newLine);
                }
                else
                    updateLine.add(line.get(i));
            }

            FileWriter output = new FileWriter(textFile);
            int index =0;
            while(index < updateLine.size()) {
                output.write(updateLine.get(index));
                if(index != (line.size()-1))
                    output.write("\n");
                index = index +1;
            }
            output.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void delete(String payload) {
        File textFile = new File(System.getProperty("user.dir"), "ServerData/Cellphones.txt");
        try {
            Scanner myReader = new Scanner(textFile);
            ArrayList<String> line = new ArrayList<>();
            while (myReader.hasNextLine()) {
                line.add(myReader.nextLine());
            }

            List<Object> values = constructPayload(payload);
            //(String) tokenInfo.get(0), (int) tokenInfo.get(3)

            String name = (String) values.get(0);
            String cell = (String) values.get(1);


            System.out.println("DELETING...");
            System.out.println(name + " : " + cell);

            for(int i=0; i< line.size();i++){

                List<Object> linevalues = analyseLine(line.get(i));

                String tname = (String) linevalues.get(0);
                String tcell = (String) linevalues.get(1);

                System.out.println(name + " : " + cell);
                System.out.println(tname + " : " + tcell);

                if(tname.equals(name) && tcell.equals(cell)){
                    System.out.println("Dele happend 3 ...............................................................................................");
                    line.remove(i); //delete
                    break;
                }
            }

            FileWriter output = new FileWriter(textFile);
            int index =0;
            while(index < line.size()) {
                output.write(line.get(index));
                if(index != (line.size()-1))
                    output.write("\n");
                index = index +1;
            }
            output.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void search(String payload) {
        File textFile = new File(System.getProperty("user.dir"), "ServerData/Cellphones.txt");
        try {
            Scanner myReader = new Scanner(textFile);
            ArrayList<String> line = new ArrayList<>();
            ArrayList<String> searchLine = new ArrayList<>();
            while (myReader.hasNextLine()) {
                line.add(myReader.nextLine());
            }

            List<Object> values = constructPayload(payload);
            String name = (String) values.get(0);
            String cell = (String) values.get(1);

            System.out.println("SEARCHING...");


            for(int i=0; i< line.size();i++){
                List<Object> linevalues = analyseLine(line.get(i));
                String tname = (String) linevalues.get(0);
                String tcell = (String) linevalues.get(1);

                System.out.println(name + " : " + cell);
                System.out.println(tname + " : " + tcell);

                if(tname.equalsIgnoreCase(name) && tcell.equalsIgnoreCase(cell)){
                    if(!searchLine.contains(line.get(i)))
                        searchLine.add(line.get(i));
                }
                if(tname.equalsIgnoreCase(name) || tname.toLowerCase(Locale.ROOT).contains(name.toLowerCase(Locale.ROOT))){
                    if(!searchLine.contains(line.get(i)))
                        searchLine.add(line.get(i));
                }
                if(tcell.equalsIgnoreCase(cell) || tcell.toLowerCase(Locale.ROOT).contains(cell.toLowerCase(Locale.ROOT))){
                    if(!searchLine.contains(line.get(i)))
                        searchLine.add(line.get(i));
                }
            }


            File searchTextFile = new File(System.getProperty("user.dir"), "searchedPhones.txt");
            if(!searchTextFile .exists()) {
                try {
                    FileWriter output = new FileWriter(searchTextFile);
                    output.write("");
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            FileWriter output = new FileWriter(searchTextFile);
            int index =0;
            while(index < searchLine.size()) {
                output.write(searchLine.get(index));
                if(index != (line.size()-1))
                    output.write("\n");
                index = index +1;
            }
            output.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Object> constructPayload(String payload){
        System.out.println("CHECKING CONSTRTION ...");
        String name = "null";
        String cell = "null";
        String contentType = "null";
        String fileName = "null";
        ArrayList<String> data = new ArrayList<>();

        boolean[] changed = new boolean[4];

        changed[0] = false;
        changed[1] = false;
        changed[2] = false;
        changed[3] = false;


        String content = "Content-Disposition:";
        int conDIS = payload.indexOf(content);
        ArrayList<String> items = new ArrayList<>();

        int index = 0;
        String lineItem = "";
        while(index < payload.length()) {
            if (index == conDIS){
                items.add(lineItem);
                lineItem = content;
                index = index + content.length();
                payload = payload.substring(index, payload.length()); // cut it
                index = 0;
                conDIS = payload.indexOf(content);
            }
            else {
                lineItem =lineItem + payload.charAt(index);
                index = index + 1;
            }
        }
        items.add(lineItem);


        for(int i =0; i< items.size();i++){
            String line  = items.get(i);
            //System.out.println("check by index : " + i + " - " + line);
        }


        String boundry = items.get(0);


        //line by line
        //ArrayList<String> temp = new ArrayList<>();
        //temp.add(items.get(0));
        for(int i =0; i< items.size();i++){
            String line  = items.get(i);
            int innerIndex = 0;
            String newLine = ""; //String.valueOf(line.charAt(innerIndex));

            ArrayList<String> values = new ArrayList<>();


            while (innerIndex < line.length()){ // separate into lines
                if(line.charAt(innerIndex) == '\n') {
                    values.add(newLine);
                    newLine = "";
                    innerIndex = innerIndex + 1;
                }

                if(innerIndex < line.length())
                    newLine = newLine + line.charAt(innerIndex);

                innerIndex = innerIndex + 1;
            }



            System.out.println("Values of .............. " + i+ " - Line") ;
            boolean datafound =  false;
            for(int j=0; j < values.size(); j++){//check lines
                System.out.println("value : " + j + " - " + values.get(j));

                if(values.get(j).contains(boundry.substring(1,boundry.length()-1))){
                    System.out.println("IS BOUNDRY");
                    continue;
                }

                if((values.get(j).length() <= 1) || values.get(j).isEmpty() ==true ) {
                    System.out.println("FOUND EMPTY LINE");
                    datafound = true;
                    j = j +1;
                }


                if( datafound == true){
                    System.out.println("DATA FOUND");
                    //System.out.println(fileData);
                    System.out.println(values.get(j));
                    if(i == 1 && values.get(j).indexOf("-") > -1){

                        name = values.get(j).substring(0,values.get(j).indexOf("-"));
                        cell = values.get(j).substring(values.get(j).indexOf("-")+1, values.get(j).length()-1);
                        System.out.println(values.get(j));
                        System.out.println(name);
                        System.out.println(cell);

                        break;
                    }
                    if(i == 1) {
                        name = values.get(j).substring(0, values.get(j).length() - 1);
                    }
                    else if(i == 2) {
                        cell = values.get(j).substring(0, values.get(j).length() - 1);
                        if (i == items.size()-1)
                            break;
                    }


                    else if(i == 3)
                        data.add(values.get(j));

                    System.out.println(name);
                    System.out.println(cell);

                }
                else {
                    /*if (values.get(j).indexOf('"') > -1) {
                        int lineIndex = 0;
                        boolean foundFirst = false;

                        while (lineIndex < values.get(j).length()) {
                            if (values.get(j).charAt(lineIndex) == '"') {
                                String tvalue = "";
                                lineIndex = lineIndex + 1;
                                while (lineIndex < values.get(j).length() && values.get(j).charAt(lineIndex) != '"') {
                                    tvalue = tvalue + values.get(j).charAt(lineIndex);
                                    lineIndex = lineIndex + 1;
                                }

                                if (foundFirst == false) {
                                    foundFirst = true;
                                    if (i == 1)
                                        name = tvalue;
                                    else if (i == 2)
                                        cell = tvalue;

                                } else if (foundFirst == true) {
                                    if (i == 3) {
                                        fileName = tvalue;
                                    }
                                }
                            }
                            lineIndex = lineIndex + 1;
                        }
                    } */
                    if (values.get(j).indexOf('"') == -1) {
                        if (i == 3)
                            contentType = values.get(j).substring(0,values.get(j).length()-1);
                    } else {

                        if (i ==3) {
                            String fileFind = "filename=\"";
                            int fileIndex = values.get(j).indexOf(fileFind );
                            int start = fileIndex + fileFind.length();

                            fileName = values.get(j).substring(start,  values.get(j).length()-2);
                            //System.out.println("FIle Found : " + fileName);


                            /*int lineIndex = 0;
                            boolean foundFirst = false;

                            while (lineIndex < values.get(j).length()) {
                                if (values.get(j).charAt(lineIndex) == '"') {
                                    String tvalue = "";
                                    lineIndex = lineIndex + 1;
                                    while (lineIndex < values.get(j).length() && values.get(j).charAt(lineIndex) != '"') {
                                        tvalue = tvalue + values.get(j).charAt(lineIndex);
                                        lineIndex = lineIndex + 1;
                                    }

                                    if (foundFirst == true) {
                                        fileName = tvalue;
                                    }
                                    if (foundFirst == false) {
                                        foundFirst = true;
                                    }
                                    lineIndex = lineIndex + 1;
                                }
                            }*/
                        }
                    }
                }
            }

        }

        /*name = name.substring(0,name.length()-1);
        cell = cell.substring(0,cell.length()-1);
        contentType= contentType.substring(0,contentType.length()-1);*/

        if((name.length() <= 1) || name.isEmpty() ==true )
            name = "null";
        if((cell.length() <= 1) || cell.isEmpty() ==true )
            cell = "null";
        if((fileName.length() <= 1) || fileName.isEmpty() ==true )
            fileName = "null";


        //System.out.println(fileName);
        //fileName = "profile1.jpeg";
        if(fileName != "null"){ // file found
            String mime = contentType.substring(contentType.indexOf("/"),contentType.length());
            if(!MimeTypes.contains(mime))
                MimeTypes.add(mime);

            saveFile(fileName, data, boundry);
        }


        System.out.println(name + " : " + cell + " : " + fileName + " : " + contentType);
        return Arrays.asList(name, cell , fileName);



        /*******************************1
        ArrayList<String> temp = new ArrayList<>();
        temp.add(items.get(0));
        for(int i =1; i< items.size();i++){
            String line  = items.get(i);
            int chopIndex = line.indexOf(boundry);
            if(chopIndex != -1)
                line = line.substring(0, chopIndex);
            temp.add(line);
        }
        items = temp;


        /*temp = new ArrayList<>();
        temp.add(items.get(0));
        for(int i =1; i< items.size();i++){
            String line  = items.get(i);
            int chopIndex = line.indexOf(";");
            if(chopIndex != -1)
                line = line.substring(chopIndex+1, line.length());
            temp.add(line);
        }
        items = temp;*





        String item[] = new String[items.size()]; // parrall arrays
        String value[] = new String[items.size()];

        for(int i =0; i< items.size();i++){
            String line  = items.get(i);
            int eqSign = line.indexOf("name=");
            System.out.println("line by index : " + i + " - " + line);
            //item[i] = line.substring(0,eqSign);
            //value[i] = line.substring(eqSign+1, line.length());
        }

        temp = new ArrayList<>();
        temp.add(items.get(0));
        for(int i =1; i< items.size();i++){
            String line  = items.get(i);
            ArrayList<String> values = new ArrayList<>();
            index = 0;
            while(index < line.length()){
                if(line.charAt(index) == '"'){
                    String tvalue = "";
                    index = index + 1;
                    while(index < line.length() && line.charAt(index) != '"'){
                        tvalue = tvalue + line.charAt(index);
                        index = index +1;
                    }
                    index = index +1;
                    values.add(tvalue);
                    if(index < line.length() ) {
                        line = line.substring(index, line.length());
                        index = 0;
                    }
                }
                else
                    index = index +1;
            }
            temp.add(line);
        }
        items = temp;

        for(int i =0; i< items.size();i++){
            String line  = items.get(i);
            System.out.println("line by index : " + i + " - " + line);
            line.replaceAll("[\\n\\t ]", "");
            System.out.println("line by index : " + i + " - " + line);
        }*/

        /****************************2
        Payload data is: ------WebKitFormBoundaryOp3AgNs2O8YD1GEb
        Content-Disposition: form-data; name="userName"

        Check User
        ------WebKitFormBoundaryOp3AgNs2O8YD1GEb
        Content-Disposition: form-data; name="cellNumber"

        079 302 5674
        ------WebKitFormBoundaryOp3AgNs2O8YD1GEb
        Content-Disposition: form-data; name="profilePicture"; filename="profile3.jpg"
        Content-Type: image/jpeg

        ���� JFIF      �� C
         */

        /****************************3
        ArrayList<String> items = new ArrayList<>();
         *
        boolean foundDelete = false;

        int index = 0;
        String lineItem = "";
        while(index < payload.length()) {
            if (payload.charAt(index) =='&'){
                items.add(lineItem);
                lineItem = "";
                index = index + 1;
            }
            else {
                lineItem =lineItem + payload.charAt(index);
                index = index + 1;
            }
        }
        items.add(lineItem);

        String item[] = new String[items.size()]; // parrall arrays
        String value[] = new String[items.size()];

        for(int i =0; i< items.size();i++){
            String line  = items.get(i);
            int eqSign = line.indexOf("=");
            item[i] = line.substring(0,eqSign);
            value[i] = line.substring(eqSign+1, line.length());
        }

        String name = "null";
        String cell = "null";
        String profile = "null";

        System.out.println("..............................   "+ item[0] + " : " + value[0]);

        for(int i =0; i < item.length; i++){
            if(item[0].equals("userData")){
                int dash = value[0].indexOf("-");
                name = value[0].substring(0, dash);
                cell = value[0].substring(dash+1,value[0].length());
                profile = "null";
                for(int j=0; j < name.length();j++)
                    if (name.charAt(j) == '+')
                        name = name.substring(0,j) + " " + name.substring(j+1, name.length());
                for(int j=0; j < cell.length();j++)
                    if (cell.charAt(j) == '+')
                        cell = cell.substring(0,j) + " " + cell.substring(j+1, cell.length());

                System.out.println("CHECKING CONSTRUCTION ...");
                System.out.println(name + " : " + cell + " : " + profile);
                return Arrays.asList(name, cell , profile);
            }else{
                for(int j=0; j < value[i].length();j++)
                    if (value[i].charAt(j) == '+')
                        value[i] = value[i].substring(0,j) + " " + value[i].substring(j+1, value[i].length());
                if(i == 0)
                    name = value[i];
                if(i == 1)
                    cell = value[i];
                if(i == 2)
                    profile = value[i];
            }
        }


        /*String values = payload.substring(payload.indexOf('=')+1, payload.length());
        name = values.substring(0, values.indexOf('-'));
        cell = values.substring(values.indexOf('-')+1, values.length());
        for(int i=0; i < name.length();i++)
            if (name.charAt(i) == '+')
                name = name.substring(0,i-1) + " " + cell.substring(i+1, cell.length());
        for(int i=0; i < cell.length();i++)
            if (cell.charAt(i) == '+')
                cell = cell.substring(0,i-1) + " " + cell.substring(i+1, cell.length());
        System.out.println("CHECKING CONSTRTION ...");
        System.out.println(name + " : " + cell + " : " + profile);

        return Arrays.asList(name, cell , profile);*/
    }


    private void saveFile(String fileName, ArrayList<String> fileData, String boundry) {

        /*****************PART
        ArrayList<byte[]> byteArray = new ArrayList<>();
        String bytesLines = "";

        String extension = fileName.substring(fileName.indexOf(".")+1, fileName.length());

        System.out.println("FILE TO BE SAVED");
        System.out.println(fileName);
        System.out.println();

        int length = 0;
        if(fileData.get(fileData.size()-1).contains(boundry.substring(1,boundry.length()-1)))
            length = fileData.size() -1;
        else
            length = fileData.size();

        int count = 0;
        for(int i =0; i < length; i++)
            count = count + fileData.get(i).length();

        String[] stringData = new String[count];

        int index =0;
        for(int i = 0; i < length ; i++) {
            //byteArray.add(fileData.get(i).getBytes());
            //System.out.println(fileData.get(i));
            int stringIndex = 0;
            while ( stringIndex < fileData.get(i).length()){
                stringData[index] = String.valueOf(fileData.get(i).charAt(stringIndex));
                stringIndex= stringIndex +1;
                index = index +1;
            }
        }

        String bigGuy = "";
        for (int i=0; i < stringData.length ; i++)
            bigGuy = bigGuy + stringData[i];

        byte[] byteData = bigGuy.getBytes(StandardCharsets.UTF_8); */



         ArrayList<byte[]> byteArray = new ArrayList<>();
         String bytesLines = "";

         String extension = fileName.substring(fileName.indexOf(".")+1, fileName.length());

         System.out.println("FILE TO BE SAVED");
         System.out.println(fileName);
         System.out.println();

         int length = 0;
         if(fileData.get(fileData.size()-1).contains(boundry.substring(1,boundry.length()-1)))
            length = fileData.size() -1;
         else
            length = fileData.size();


        for(int i = 0; i < length ; i++) {
            byteArray.add(fileData.get(i).getBytes());
        }

        int count = 0;
        for(int i =0; i < byteArray.size(); i++)
            count = count + byteArray.get(i).length;

        System.out.println("COUNTS : "  + count);
        byte[] byteData = new byte[count];

        int index =0;
        for(int i =0; i < byteArray.size(); i++){
            int byteIndex = 0;
            while (byteIndex < byteArray.get(i).length){
                byteData[index] = byteArray.get(i)[byteIndex];
                byteIndex = byteIndex +1;
                index = index +1;
            }
        }







        for(int i=0; i < byteData.length; i++)
            System.out.print(byteData[i]);
        System.out.println();

        try {
            /*int BUFFER_SIZE = 4096;
            File imageFile =  new File("ServerData/" + fileName);
            FileOutputStream outputStream = new FileOutputStream(imageFile);

            byte bytesRead ;
            byte[] buffer = new byte[BUFFER_SIZE];
            //outputStream.w
            UploadFileStream.
            while ((bytesRead = UploadFileStream.readByte()) == ) {
                outputStream.write(buffer, 0, bytesRead);
            }

            System.out.println("Done");*/


            /*BufferedImage bImage = ImageIO.read(new File("ServerData/" + fileName)); //new ByteArrayInputStream(byteData)
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bImage, extension, bos );*/

            //TODO fix
            //byte [] data = bos.toByteArray();
            //InputStream bis = new ByteArrayInputStream(byteData);
            //System.out.println(bis);


            BufferedImage Image;

            ImageIcon icon = new ImageIcon(byteData);
            java.awt.Image img = icon.getImage();
            System.out.println(icon);
            System.out.println(img);
            //BufferedImage b = new BufferedImage();//new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);

            if(img.getWidth(null) > -1) {
                System.out.println("WE WANT TO SEE");
                Image = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            }
            else {
                System.out.println("WE DON'T WANT TO SEE");
                Image = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
            }
            System.out.println(Image);

            ImageIO.write(Image, extension, new File("ServerData/" + fileName) );
            System.out.println("image created");

            /*for(int i=0; i < MainDataBytes.length; i++)
                System.out.print(MainDataBytes[i]);
            System.out.println();

            InputStream imageInput = new ByteArrayInputStream(connect.getInputStream().readAllBytes());
            DataInputStream dis = new DataInputStream(imageInput);
            System.out.println(dis.toString());
            int len = dis.readInt();
            System.out.println("Image Size: " + len/1024 + "KB");

            byte[] data = new byte[len]; //UploadFileData
            dis.readFully(data);
            dis.close();
            //UploadFileStream.close();

            InputStream ian = new ByteArrayInputStream(data);
            BufferedImage bImage = ImageIO.read(ian);

            ImageIO.write(bImage, extension, new File("ServerData/" + fileName) );*/







        } catch (IOException e) {
            e.printStackTrace();
        }




        /*File textFile = new File(System.getProperty("user.dir"), "ServerData/" + fileName);

        if(!textFile.exists()) {
            try {
                FileWriter output = new FileWriter(textFile);
                output.write("");
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/



        /*FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];

        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null)
                fileIn.close();
        }

        return fileData;*/
    }

    private List<Object> analyseLine(String line){

        ArrayList<String> items = new ArrayList<>();
        int index = 0;
        String lineItem = "";
        while(index < line.length()) {
            if (line.charAt(index) ==','){
                items.add(lineItem);
                lineItem = "";
                index = index + 1;
            }
            else {
                lineItem =lineItem + line.charAt(index);
                index = index + 1;
            }
        }
        items.add(lineItem);

        String name = items.get(0);
        String cell = items.get(1);
        String profile = items.get(2);

        //String tname = line.substring(0, line.indexOf(','));
        //String tcell = line.substring(payload.indexOf(',')+1, line.length());

        System.out.println("CHECKING LINE ...");
        System.out.println(name + " : " + cell + " : " + profile);
        return Arrays.asList(name, cell , profile);
    }
}
