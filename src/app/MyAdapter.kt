import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.followme.
import com.example.followme.R
import kotlinx.android.synthetic.main.roboter_item.view.*
import androidx.recyclerview.widget.RecyclerView


class RobotAdapter(private val robots: MutableList<Robot>) : RecyclerView.Adapter<RobotAdapter.RobotViewHolder>() {

    class RobotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RobotViewHolder {
        return RobotViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.roboter_item, parent, false))
    }


    fun addRobot(robot: Robot) {
        robots.add(robot)
        notifyItemInserted(robots.size - 1)
    }


    fun deleteRobot() {
        // robots.removeAll { robot -> robots.  }
        // notifyDatSetChanged()
    }



    override fun onBindViewHolder(holder: RobotViewHolder, position: Int) {
        val curRobot = robots[position]
        holder.itemView.apply {
            tvRoboter.text = curRobot.name

        }
    }

    override fun getItemCount(): Int {
        return robots.size
    }
}