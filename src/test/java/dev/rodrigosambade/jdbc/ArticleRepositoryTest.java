package dev.rodrigosambade.jdbc;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
class ArticleRepositoryTest{
    @Test void inserts()throws Exception{
        try(var c=DemoDatabase.open()){
            var r=new ArticleRepository();
            int before=r.all(c).size();
            var a=r.insert(c,"Nuevo","Demo",new BigDecimal("12.50"));
            assertTrue(a.id()>0);
            assertEquals(before+1,r.all(c).size());
        }
    }
}
