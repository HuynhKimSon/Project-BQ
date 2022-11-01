package vn.com.irtech.irbot.business.dto;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import vn.com.irtech.core.common.text.Convert;
import vn.com.irtech.irbot.business.domain.Invoice;
import vn.com.irtech.irbot.business.domain.InvoiceDetail;

public class InvoiceImport extends Invoice {

	private static final long serialVersionUID = 1L;

	private List<InvoiceDetail> details;

	public List<InvoiceDetail> getDetails() {
		return details;
	}

	public void setDetails(List<InvoiceDetail> details) {
		this.details = details;
	}

	@JsonIgnore
	public Long calcTotalAmount() {
		if (CollectionUtils.isEmpty(details)) {
			return 0L;
		}
		
		Long result = 0L;
		for (InvoiceDetail invDetail : details) {
			result += invDetail.calcTotalAmount();
		}
		return result;
	}
}
