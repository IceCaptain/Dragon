package com.java.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 通知实体
 * @author zxy
 *
 */
@Entity
@Table(name="t_notice")
public class Notice {
	
	@Id
	@GeneratedValue
	private Integer id;//编号
	
	@Column(length=100)
	private String title;//标题
	
//	sqlite 数据库注释掉	
//	@Lob
//	@Column(columnDefinition="TEXT")
	private String content;//内容
	
//	@Temporal(TemporalType.DATE)
//	@Column(columnDefinition="timestamp")
	private Date releaseDate;//发布时间

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

}
