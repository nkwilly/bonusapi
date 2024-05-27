package com.systemedebons.bonification.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation = "admins")
@Data
public class Administrator {

  @Id
  private  String  id;
  private String  username;
  private String  email;
  private String  password;


}
