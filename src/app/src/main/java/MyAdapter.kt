import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.followme.R
import kotlinx.android.synthetic.main.roboter_item.view.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView


class MyAdapter(private val robots: java.util.ArrayList<Robot>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val ivRoboter : ShapeableImageView = itemView.findViewById(R.id.ivRoboter)
        val tvRoboter : TextView = itemView.findViewById(R.id.tvRoboter)
        val tvPfeil : TextView = itemView.findViewById(R.id.tvPfeil)
    }

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