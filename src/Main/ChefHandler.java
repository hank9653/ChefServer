package Main;

import Services.Service;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
/**
 * Created by hank9653 on 2016/4/28.
 */
public class ChefHandler extends Thread {
    private Socket socket;
    private PrintWriter respondToClient;

    public ChefHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader requestFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            respondToClient = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"),true);

            while(true){
                String receiveMessage = requestFromClient.readLine();
                JSONObject getClientRequest = new JSONObject(receiveMessage);
                System.out.println(receiveMessage);
                if (getClientRequest.getString("requestServiceType").equals("ChefClientService")){
                    Service service = new Service(this);
                    service.SelectService(getClientRequest.get("requestService").toString());
                }else if(getClientRequest.getString("requestServiceType").equals("Account")){

                }else{
                    feedbackToClient("No Service");
                }
            }
        } catch (IOException e) {
            feedbackToClient("ChefHandler error:"+e.toString());
        } catch (JSONException e) {
            feedbackToClient("ChefHandler Json error:"+e.toString());
        }
    }

    public void feedbackToClient(String message) {
        respondToClient.println(message);
    }
}