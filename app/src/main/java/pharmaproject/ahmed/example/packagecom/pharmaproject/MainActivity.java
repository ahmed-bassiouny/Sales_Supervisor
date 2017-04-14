package pharmaproject.ahmed.example.packagecom.pharmaproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    //private DatabaseReference mDatabase;
    FloatingActionButton a1,a2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mDatabase = FirebaseDatabase.getInstance().getReference(Information.DATABASE_NAME);
        //writeNewUser();
        a1=(FloatingActionButton)findViewById(R.id.a1);
        a2=(FloatingActionButton)findViewById(R.id.a2);
        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        a1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Done",Toast.LENGTH_SHORT).show();
                menuMultipleActions.collapse();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,Signin.class));
                finish();
            }
        });
    }
    private void writeNewUser() {
        /* Create Task
        Task task = new Task();
        task.id=2;
        task.doctorName="doctorName";
        task.description="description";
        task.locationDoctor="locationDoctor";
        task.locationEmployee="locationEmployee";
        task.taskType= TaskType.COMPLETE;
        task.time="time";

        // Create Employee
        Employee employee=new Employee();
        employee.phone="employee_phone";
        employee.name="name";
        employee.imageUrl="imageUrl";
        employee.lastLocation="lastLocation";
        employee.password="password";
        //Create Admin
        Supervisor supervisor = new Supervisor();
        Information.CurrentUserPhone= supervisor.phone="supervisor_phone";
        supervisor.area="area";
        supervisor.name="name";*/
        //mDatabase.child(supervisor.phone).setValue(supervisor);
        //mDatabase.child(supervisor.phone).child(employee.phone).setValue(employee);
        //mDatabase.child(supervisor.phone).child(employee.phone).child(task.id+"").setValue(task);
        //supervisor.insert_updateSupervisor(supervisor);
        //employee.insert_updateEmployee(employee);
        //task.insert_updateTask(employee,task);
        //task.deleteTask(employee,task);
        //employee.deleteEmployee();

        //supervisor.insert_updateSupervisor();
        //employee.insert_updateEmployee();
        //task.insert_updateTask(employee);

        //task.getTask(employee,2);
    }
}
