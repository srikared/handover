package handover;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.apache.http.HttpHeaders.USER_AGENT;

@RestController
public class MessageController {

    @Autowired
    private DataSource dataSource;

    @RequestMapping("/")
    public String index() throws Exception{
        GetChannelHistory();
        return "Greetings from Spring Boot!";
    }

    @RequestMapping(value = "/handover",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = "application/json"
    )
    public HandoverResponse handover(SlashCommandReq command) throws Exception{
        GetChannelHistory();
        Statement stmt = dataSource.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM tasks");
        String mainText ="The following tasks are left to be completed : ";
        int i=1;
        String leftOverTasks ="";
        while (rs.next()) {
            if (rs.getInt("task_completed")==0) {
                leftOverTasks += i+". " + rs.getString("task") + "\n";
                i++;
            }
        }
       return new HandoverResponse("in_channel",mainText, leftOverTasks);
    }

    void saveTask(Statement stmt,String task, int task_completed, String message_id) throws SQLException {
            String sql = "INSERT INTO tasks(task,message_id,task_completed)" +
                    "VALUES ('"+
                    task+
                    "','"+
                    message_id+
                    "',"+
                    task_completed+
                    ")";
            stmt.executeUpdate(sql);

    }

    void updateTask(Statement stmt,int task_completed, String message_id) throws SQLException{
        String sql = "Update Tasks set task_completed="+task_completed+" where message_id='"+message_id+"'";
        stmt.executeUpdate(sql);
    }

    Task getTask(Statement stmt,String messageID) throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT * FROM tasks where message_id='"+messageID+"'");
        if (!rs.next()) {
            return null;
        }
        return new Task(
                rs.getInt("id"),
                rs.getString("message_id"),
                rs.getString("task"),
                rs.getInt("task_completed")
        );
    }


    // HTTP GET request
    public void GetChannelHistory() throws Exception {
        Statement stmt = dataSource.getConnection().createStatement();
        String url = "https://slack.com/api/channels.history?token=xoxp-564962179077-563888355312-566373847718-9905d1838c203cbaf5ba47f5a074ea63&channel=CGMLRVB1V&count=20";

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);

        // add request header
        request.addHeader("User-Agent", USER_AGENT);

        HttpResponse response = client.execute(request);

        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " +
                response.getStatusLine().getStatusCode());

        ObjectMapper objectMapper = new ObjectMapper();
        ChannelHistory myObject = objectMapper.readValue(response.getEntity().getContent(), ChannelHistory.class);
        for (MessageType mt : myObject.messages) {
            if (mt.subtype !=""){
                continue;
            }
            Task t = getTask(stmt,mt.client_msg_id);
            if (t.task_completed==1){
                continue;
            }
            if (t!=null) {
                if (doesContainWhiteCheck(mt)){
                    updateTask(stmt,1,mt.client_msg_id);
                }
                else {
                    updateTask(stmt,0,mt.client_msg_id);
                }
            }else {
                if (doesContainWhiteCheck(mt)){
                    saveTask(stmt,mt.text,1,mt.client_msg_id);
                }
                else {
                    saveTask(stmt,mt.text,0,mt.client_msg_id);
                }
            }
        }
        stmt.close();
    }

    boolean doesContainWhiteCheck(MessageType mt) {
        for (Reaction r : mt.reactions) {
            if(r.name.equalsIgnoreCase("white_check_mark")){
                return true;
            }
        }
        return false;
    }
}