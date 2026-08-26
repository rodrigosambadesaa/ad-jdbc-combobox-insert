package dev.rodrigosambade.jdbc;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
public final class ArticleRepository {
    public List<Article> all(Connection c)throws SQLException{
        try(var ps=c.prepareStatement("select codigo,modelo,descripcion,precio from articulos order by codigo");
        var rs=ps.executeQuery()){
            List<Article>x=new ArrayList<>();
            while(rs.next())x.add(new Article(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getBigDecimal(4)));
            return List.copyOf(x);
        }
    }
    public Article insert(Connection c,String model,String description,BigDecimal price)throws SQLException{
        if(model==null||model.isBlank())throw new IllegalArgumentException("Modelo obligatorio");
        if(price==null||price.signum()<0)throw new IllegalArgumentException("Precio inválido");
        try(var ps=c.prepareStatement("insert into articulos(modelo,descripcion,precio,descuento,cod_marca,cod_familia) values(?,?,?,0,1,1)",Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,model.trim());
            ps.setString(2,description);
            ps.setBigDecimal(3,price);
            ps.executeUpdate();
            try(var keys=ps.getGeneratedKeys()){
                if(!keys.next())throw new SQLException("No se obtuvo la clave");
                return new Article(keys.getInt(1),model.trim(),description,price);
            }
        }
    }
}
