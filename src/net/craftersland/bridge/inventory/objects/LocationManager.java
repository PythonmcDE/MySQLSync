package net.craftersland.bridge.inventory.objects;

import net.craftersland.bridge.inventory.database.MysqlSetup;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class LocationManager {

    private String name;

    public LocationManager(String name) {
        this.name = name;
    }

    public static HashMap<String, Location> locations = new HashMap<>();

    public boolean isLocationExists(){
        try(PreparedStatement ps = MysqlSetup.getConnection().prepareStatement("SELECT * FROM locations WHERE name = ?")){
            ps.setString(1, this.name);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public void setLocsHashmap(String name){
        locations.put(name,getLocationDatabase());
    }

    public Location getLocation(){
        if(locations.containsKey(name)) {
            return locations.get(name);
        }else {
            locations.put(name, getLocationDatabase());
            return locations.get(name);
        }
    }

    public Location getLocationDatabase(){
        if(isLocationExists()){
            try (PreparedStatement ps = MysqlSetup.getConnection().prepareStatement("SELECT * FROM locations WHERE name = ?")) {
                ps.setString(1, this.name);
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    String worldname = rs.getString("world");
                    double x = rs.getDouble("x");
                    double y = rs.getDouble("y");
                    double z = rs.getDouble("z");
                    float yaw = rs.getFloat("yaw");
                    float pitch = rs.getFloat("pitch");
                    return new Location(Bukkit.getWorld(worldname), x, y, z, yaw, pitch);

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}


