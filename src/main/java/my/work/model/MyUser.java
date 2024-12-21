package my.work.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_tab")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MyUser {

	@Id
	@GeneratedValue
	private Long id;

	private String username;

	private String password;

	private String role;
}
