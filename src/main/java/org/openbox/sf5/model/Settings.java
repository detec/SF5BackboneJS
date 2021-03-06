package org.openbox.sf5.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.validator.constraints.NotEmpty;
import org.openbox.sf5.converters.TimestampAdapter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * Setting entity
 *
 * @author Andrii Duplyk
 *
 */
@Entity
@Table(name = "Settings", indexes = { @Index(columnList = "user", name = "user") })
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Settings extends AbstractDbEntity implements Serializable {

	private static final long serialVersionUID = 7055744176770843683L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "name", unique = false, nullable = false, length = 50)
	@NotEmpty
	@XmlID
	private String name;

	@Column(name = "theLastEntry", unique = false, nullable = true)
	@XmlJavaTypeAdapter(TimestampAdapter.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ", timezone = "Europe/Kiev")
	@NotNull
	private Timestamp theLastEntry;

	@ManyToOne
	@JoinColumn(name = "\"user\"", unique = false, nullable = false, foreignKey = @ForeignKey(name = "FK_User"))
	@NotNull
	@Valid
	private Users user;

	@OneToMany(mappedBy = "parent_id", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
	@OrderColumn(name = "lineNumber")
	@JsonManagedReference
	private List<SettingsConversion> conversion = new ArrayList<>();

	/**
	 *
	 * @param name
	 * @param lastEntry
	 * @param user
	 * @param conversion
	 */
	public Settings(String name, Timestamp lastEntry, Users user, List<SettingsConversion> conversion) {

		this.name = name;
		this.theLastEntry = lastEntry;
		this.user = user;
		this.conversion = conversion;

	}

	/**
	 *
	 */
	public Settings() {
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public long getId() {

		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		// we must convert object to id
		return String.valueOf(id);
	}

	public Timestamp getTheLastEntry() {
		return theLastEntry;
	}

	public void setTheLastEntry(Timestamp theLastEntry) {
		this.theLastEntry = theLastEntry;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public List<SettingsConversion> getConversion() {
		return conversion;
	}

	public void setConversion(List<SettingsConversion> conversion) {
		this.conversion = conversion;
	}

}