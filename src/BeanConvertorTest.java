import dto.SourceDTO;
import dto.TargetDTO;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

/**
 * Created by lixinyao on 2019/3/2.
 */
public class BeanConvertorTest {

    public static void main(String[] args){
        SourceDTO sourceDTO = new SourceDTO();
        sourceDTO.setId(2345678L);
        sourceDTO.setDeliveryStatus(1);
        sourceDTO.setDeliveryTime(OffsetDateTime.now());
        sourceDTO.setOrderCode(UUID.randomUUID().toString());
        sourceDTO.setPending(false);
        sourceDTO.setQutity(BigDecimal.TEN);
        sourceDTO.setLatestDeliveryTime(new Date());
        sourceDTO.setStatus(BigInteger.TEN);
        TargetDTO targetDTO = BeanConvertor.convert(sourceDTO, TargetDTO.class);
        System.out.println(targetDTO);
    }
}
