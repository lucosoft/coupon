package com.challenge.coupon.repository;

import com.challenge.coupon.exception.CouponException;
import com.challenge.coupon.model.Favorites;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FavoritesRepository {

    private final static String ERR_COUPON_SQL_ERROR_CODE = "ERR_CPN_105";
    private final static String ERR_COUPON_SQL_ERROR_DESC = "An error ocurred trying to connect with database";


    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void updateFavoritesById(String id){
        List<Favorites> favorites = jdbcTemplate.query("select * from favorites where id='" + id + "'", (rs, rowNumber) -> {
            try {
                return productMapping(rs);
            } catch (SQLException e) {
                throw new CouponException(ERR_COUPON_SQL_ERROR_CODE, ERR_COUPON_SQL_ERROR_DESC, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        });
        if(favorites.size()<1) {
            jdbcTemplate.execute("insert into favorites(id, quantity) " +
                            "values('" + id + "'," + 1 + ")");
        }else {
            jdbcTemplate.execute("update favorites set quantity=" +
                    (favorites.get(0).getQuantity() + 1) + " where id='" + id + "'");
            }
        }

    public List<Favorites> getTopFavorites(){
        return jdbcTemplate.query("select * from favorites order by quantity desc limit 5", (rs,rowNumber) -> {
            try
            {   return productMapping(rs);
            }catch(SQLException e){
                throw new RuntimeException("SQLException: ",e);
            }
        });
    }

    private Favorites productMapping(ResultSet rs) throws SQLException {
        Favorites favorites = new Favorites();

        favorites.setId(rs.getString("id"));
        favorites.setQuantity(rs.getLong("quantity"));

        return favorites;
    }
}
