//advance feature 2.2
package webserver;

import in2011.http.Request;
import in2011.http.Response;
import in2011.http.StatusCodes;
import in2011.http.EmptyMessageException;
import in2011.http.MessageFormatException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.client.utils.DateUtils;

public class WebServer  implements Runnable{

    private static int port;
    private static String rootDir;
    private Socket connection;

    public WebServer(int por, String rootDi) {
       WebServer.port = por;
        WebServer.rootDir = rootDi;
    }
    
    public WebServer(Socket connection) {
      this.connection=connection;
    }
    
    public static void start() throws IOException 
    {
        
    ServerSocket serverSocket = new ServerSocket(port);
    
    while (true)
     {
    Socket connection=serverSocket.accept();
    
     new Thread(new WebServer(connection)).start();
    
    }
    }

    public static void main(String[] args) throws IOException{
        String usage = "Usage: java webserver.WebServer <port-number> <root-dir>";
        if (args.length != 2) {
            throw new Error(usage);
           
        }
        int port;
        try {
            WebServer.port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            throw new Error(usage + "\n" + "<port-number> must be an integer");
        }
        WebServer.rootDir = args[1] ;
        
        WebServer.start();
        //server is started now implementing the task from here
        
        
        
    }

    @Override
    public void run() 
    {
        Request req=null;
     OutputStream os=null;   
        try 
        {
            InputStream is = connection.getInputStream();
            
           req = Request.parse(is);
             os = connection.getOutputStream(); 
            String methName = req.getMethod();
            String URI = req.getURI();
            String pathname = WebServer.rootDir + URI;
            Path Fullpath = Paths.get(pathname);
            Path AllPath = Fullpath.toAbsolutePath().normalize();
               
           
            
            if ("GET".equals(methName))
            {
                

 
        {
            InputStream thefile = Files.newInputStream(AllPath); 
            System.out.println(AllPath.toAbsolutePath());
                    while (true) {
                       
                        Response FileObtained = new Response(200); 
                        FileObtained.write(os);   
                         byte[] b = Files.readAllBytes(AllPath);
                        
                         os.write(b);
                         break;
                                    
            }
            }
                                  
            }
            
             else{
               Response msgo = new Response(500);  
            }
            
 
        connection.close();
            } 
        
        catch (IOException ex) { 
//                   Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("webserver.WebServer.run()");
                try {
                boolean indexed=false;
                
             
                String URI = req.getURI();
                String pathname = WebServer.rootDir + URI;
                Path Fullpath = Paths.get(pathname);
                Path AllPath = Fullpath.toAbsolutePath().normalize();
                
                File folder = new File(pathname);
                File[] listOfFiles = folder.listFiles();
                
                if(URI.length()<=1)
                {
                for(int i = 0; i < listOfFiles.length; i++) 
                {
                 if (listOfFiles[i].isFile()) 
                 {
                      if(listOfFiles[i].getName().contains("index."))
                      {
                        
                          
                         byte[] b = Files.readAllBytes(Paths.get(listOfFiles[i].getAbsolutePath()));
                        
                         os.write(b);
                         
                         
                            
                            indexed=true;
                          
                      }
                      
                 }
                
                }
                if(!indexed)
                {
                    String links="";
                   for (int i = 0; i < listOfFiles.length; i++)
                   {
                        
                    links =links+"<br><a href=\"./"+listOfFiles[i].getName()+"\">"+listOfFiles[i].getName()+"</a><br> ";
                       
                    }
                    byte[] b = links.getBytes();
                        
                         os.write(b);
                }
                
    
                }
                
                else
                {
                   String links="";
                   for (int i = 0; i < listOfFiles.length; i++)
                   {
                        
                    links =links+"<br><a href=\"./"+listOfFiles[i].getName()+"\">"+listOfFiles[i].getName()+"</a><br> ";
                       
                    }
                    byte[] b = links.getBytes();
                        
                         os.write(b);
                }
               
                  connection.close();
              
                
                    }
                
                catch (Exception ex1) 
                    {
                    Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, null, ex1);
                    }
        } 
        
        catch (MessageFormatException ex) 
        {
            Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
                

         }
            
         
       
            
    
    }
    
