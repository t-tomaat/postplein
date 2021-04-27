package nl.ruben.postplein.users;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Entity(name = "users")
@ApiModel(description = "Een user die een post kan maken")
public class User {

	@Id
	@GeneratedValue
	@ApiModelProperty("ID toegekend door het systeem")
	private Long id;
	
	@Version
	private int version;
	
	@Size(min = 3, max = 99)
	@ApiModelProperty(value = "De volledige naam van de user", example = "Pietje Puk")
	private String name;
	
	@ApiModelProperty("Datum/tijd van invoer, toegekend door het systeem")
	private LocalDateTime ingevoerdOp;
	
	@ApiModelProperty("Datum/tijd van wijziging, toegekend door het systeem")
	private LocalDateTime gewijzigdOp;
	
}
