import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.followme.R
import kotlinx.android.synthetic.main.roboter_item.view.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

//Suuuper nervige Sache
// Falls etwas unklar ist, bitte RecyclerView-Tutorial mal anschauen
class MyAdapter(private val robots: java.util.ArrayList<Robot>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    //Basically wird hier angegben welche Items, der RecyclerView halten soll
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        // Das ist, aus was unser Roboter OBjekt besteht
        val ivRoboter : ShapeableImageView = itemView.findViewById(R.id.ivRoboter)
        val tvRoboter : TextView = itemView.findViewById(R.id.tvRoboter)
        val tvPfeil : TextView = itemView.findViewById(R.id.tvPfeil)
    }
    // Hier wird der gerade initialisierte ViewHolder kreiert
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.roboter_item, parent, false)
        return MyViewHolder(itemView)
    }


    fun addRobot(robot: Robot) {
        robots.add(robot)
        notifyItemInserted(robots.size - 1)
    }


    fun deleteRobot() {
        // robots.removeAll { robot -> robots.  }
        // notifyDatSetChanged()
    }


    // Hier wird der ViewHolder gebindet, ist son Android Ding, ist dafür da, damit es angezeit werden kann
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = robots[position]
        holder.ivRoboter.setImageResource(currentItem.ivRoboter)
        holder.tvRoboter.text = currentItem.robotName
        holder.tvPfeil.text = currentItem.tvPfeil

    }

    override fun getItemCount(): Int {
        return robots.size
    }
}