package cn.keyi.bye.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="needsplitprefix")
public class Needsplitprefix implements Serializable {
	private static final long serialVersionUID = -5052658152387084058L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "prefix_id")
	private Long prefixId;
	// 具有下级零件的图号标记
	@Column(unique = true, name = "prefix_label")
	private String prefixLabel;
	// 是否为产品的图号标记
	@Column(name = "prefix_product")
	private Boolean prefixProduct = Boolean.FALSE;
	// 创建时间
	@Column(name = "create_time")
	private LocalDateTime createTime;
	// 创建人
	@Column(name = "create_by")
	private String createBy;
	// 修改时间
	@Column(name = "update_time")
	private LocalDateTime updateTime;
	// 修改人
	@Column(name = "update_by")
	private String updateBy;
	
	public Long getPrefixId() {
		return prefixId;
	}
	public void setPrefixId(Long prefixId) {
		this.prefixId = prefixId;
	}
	public String getPrefixLabel() {
		return prefixLabel;
	}
	public void setPrefixLabel(String prefixLabel) {
		this.prefixLabel = prefixLabel;
	}
	public Boolean getPrefixProduct() {
		return prefixProduct;
	}
	public void setPrefixProduct(Boolean prefixProduct) {
		this.prefixProduct = prefixProduct;
	}
	public LocalDateTime getCreateTime() {
		return createTime;
	}
	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public LocalDateTime getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}	
}
