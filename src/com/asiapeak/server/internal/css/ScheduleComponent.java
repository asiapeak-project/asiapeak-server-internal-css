package com.asiapeak.server.internal.css;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.asiapeak.server.internal.css.dao.entity.Product;
import com.asiapeak.server.internal.css.dao.repo.ProductRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ScheduleComponent {

	@Autowired
	ProductRepo productRepo;

	private final List<String> reminderSearchFields = Arrays.asList( //
			"保固到期日" //
	);

	/**
	 * 維護到期提醒
	 * 
	 * @throws JsonProcessingException
	 * @throws JsonMappingException
	 */
	@Scheduled(cron = "0 0 8 ? * MON")
	public void MaintenanceDueReminder() throws Exception {

		final Date now = new Date();

		List<Product> products = productRepo.findAll();

		List<MailRow> expiredProducts = new ArrayList<>();
		List<MailRow> d7Products = new ArrayList<>();
		List<MailRow> d30Products = new ArrayList<>();
		List<MailRow> d60Products = new ArrayList<>();
		List<MailRow> d90Products = new ArrayList<>();

		List<MailRow> errorProducts = new ArrayList<>();

		for (Product product : products) {

			String infoColumns = product.getInfoColumns();
			String valueColumns = product.getInfoValues();

			List<String> infoColumnList = parseJsonArray(infoColumns);
			List<String> valueColumnList = parseJsonArray(valueColumns);

			for (int i = 0; i < infoColumnList.size(); i++) {
				String infoColumn = infoColumnList.get(i);
				String valueColumn = valueColumnList.get(i);
				if (reminderSearchFields.contains(infoColumn.trim())) {

					MailRow row = new MailRow(product.getCustomer().getDname(), product.getSubject(), valueColumn);

					Date date = formateDate(valueColumn);
					if (date == null) {
						errorProducts.add(row);
						continue;
					}
					if (isExpired(now, date)) {
						expiredProducts.add(row);
					} else if (isAboutDue(now, date, 7)) {
						d7Products.add(row);
					} else if (isAboutDue(now, date, 30)) {
						d30Products.add(row);
					} else if (isAboutDue(now, date, 60)) {
						d60Products.add(row);
					} else if (isAboutDue(now, date, 90)) {
						d90Products.add(row);
					}

				}
			}

		}

		StringBuilder sb = new StringBuilder();

		if (expiredProducts.size() > 0) {
			sb.append("</br>");
			sb.append(buildTable("已經過期之清單", expiredProducts));
			sb.append("</br>");
		}

		if (d7Products.size() > 0) {
			sb.append("</br>");
			sb.append(buildTable("到期日 7 天之內清單", d7Products));
			sb.append("</br>");
		}

		if (d30Products.size() > 0) {
			sb.append("</br>");
			sb.append(buildTable("到期日 30 天之內清單", d30Products));
			sb.append("</br>");
		}

		if (d60Products.size() > 0) {
			sb.append("</br>");
			sb.append(buildTable("到期日 60 天之內清單", d60Products));
			sb.append("</br>");
		}

		if (d90Products.size() > 0) {
			sb.append("</br>");
			sb.append(buildTable("到期日 90 天之內清單", d90Products));
			sb.append("</br>");
		}

		if (expiredProducts.size() > 0) {
			sb.append("</br>");
			sb.append(buildTable("日期格式錯誤之清單(需修正成 yyyy/MM/dd 格式)", expiredProducts));
			sb.append("</br>");
		}

		if (sb.length() == 0) {
			sb.append("</br>");
			sb.append("<p>目前無任何快到期的客戶！</p>");
			sb.append("</br>");
		}

		sb.append("</br>");
		sb.append("</br>");
		sb.append("</br>");
		sb.append("</br>");

		sb.append("<p>此信件是由系統自動寄出，請勿回信。</p>");
		sb.append("</br>");

		sendMail(new String[] { //
				"fred@asiapeak.com", //
				"derrek@asiapeak.com",//
		}, "玉山科技-客戶服務系統-保固到期日通知信件", sb.toString());

	}

	private static class MailRow {
		String customerName;
		String productName;
		String time;

		public MailRow(String customerName, String productName, String time) {
			this.customerName = customerName;
			this.productName = productName;
			this.time = time;
		}
	}

	private static String buildTable(String title, List<MailRow> rows) {
		StringBuilder sb = new StringBuilder();

		String style = "border: 1px solid black;border-collapse: collapse;";

		sb.append("<table style='width: 80%'>");

		sb.append("<thead>");

		sb.append("    <tr>");
		sb.append("       <th style='" + style + "' colspan='3'>" + title + "</th>");
		sb.append("    </tr>");

		sb.append("    <tr>");
		sb.append("        <th style='" + style + "text-align:left;'>客戶名稱</th>");
		sb.append("        <th style='" + style + "text-align:left;'>產品名稱</th>");
		sb.append("        <th style='" + style + "text-align:left;'>到期日期</th>");
		sb.append("    </tr>");

		sb.append("</thead>");

		sb.append("<tbody>");

		rows.forEach(row -> {
			sb.append("    <tr>");
			sb.append("        <td style='" + style + "'>" + row.customerName + "</td>");
			sb.append("        <td style='" + style + "'>" + row.productName + "</td>");
			sb.append("        <td style='" + style + "'>" + row.time + "</td>");
			sb.append("    </tr>");
		});
		sb.append("</tbody>");

		sb.append("</table>");

		return sb.toString();
	}

	private static void sendMail(String[] recipients, String subject, String htmlContent) {

		String smtpHost = "smtpauths.asiapeak.com";
		String smtpPort = "25";
		String username = "apcss@asiapeak.com";
		String password = "arngpianpnw3fsfe";

		Properties props = new Properties();
		props.put("mail.smtp.host", smtpHost);
		props.put("mail.smtp.port", smtpPort);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "false");
		props.put("mail.smtp.ssl.enable", "false");

		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			InternetAddress[] addressList = Arrays.stream(recipients).map(email -> {
				try {
					return new InternetAddress(email);
				} catch (AddressException e) {
					throw new RuntimeException(e);
				}
			}).toArray(InternetAddress[]::new);
			message.setRecipients(Message.RecipientType.TO, addressList);
			message.setSubject(subject);
			message.setContent(htmlContent, "text/html; charset=UTF-8");
			Transport.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 判斷是否過期，只比較年月日。同一天視為過期。
	 *
	 * @param now
	 * @param date
	 * @return
	 */
	private boolean isExpired(Date now, Date date) {
		Calendar nowCal = Calendar.getInstance();
		nowCal.setTime(now);
		nowCal.set(Calendar.HOUR_OF_DAY, 0);
		nowCal.set(Calendar.MINUTE, 0);
		nowCal.set(Calendar.SECOND, 0);
		nowCal.set(Calendar.MILLISECOND, 0);

		Calendar dateCal = Calendar.getInstance();
		dateCal.setTime(date);
		dateCal.set(Calendar.HOUR_OF_DAY, 0);
		dateCal.set(Calendar.MINUTE, 0);
		dateCal.set(Calendar.SECOND, 0);
		dateCal.set(Calendar.MILLISECOND, 0);

		return dateCal.compareTo(nowCal) <= 0;
	}

	/**
	 * 判斷是否快到期，比較年月日。
	 *
	 * <pre>
	 * date 小於 now 回傳 true
	 * now=2025/01/01 date=2025/01/07 day=7 會回傳 true
	 * now=2025/01/01 date=2025/01/08 day=7 會回傳 false
	 *
	 * </pre>
	 *
	 *
	 * @param now  當前時間
	 * @param date 比較的時間
	 * @param days 日數
	 * @return
	 */
	private boolean isAboutDue(Date now, Date date, int days) {
		Calendar nowCal = Calendar.getInstance();
		nowCal.setTime(now);
		nowCal.set(Calendar.HOUR_OF_DAY, 0);
		nowCal.set(Calendar.MINUTE, 0);
		nowCal.set(Calendar.SECOND, 0);
		nowCal.set(Calendar.MILLISECOND, 0);

		Calendar dateCal = Calendar.getInstance();
		dateCal.setTime(date);
		dateCal.set(Calendar.HOUR_OF_DAY, 0);
		dateCal.set(Calendar.MINUTE, 0);
		dateCal.set(Calendar.SECOND, 0);
		dateCal.set(Calendar.MILLISECOND, 0);

		// 如果 date 小於 now，直接回傳 true
		if (dateCal.compareTo(nowCal) < 0) {
			return true;
		}

		// 計算 now + days 天
		Calendar thresholdCal = (Calendar) nowCal.clone();
		thresholdCal.add(Calendar.DAY_OF_MONTH, days);

		// 判斷 date 是否在門檻值內
		return dateCal.compareTo(thresholdCal) <= 0;
	}

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

	private Date formateDate(String str) {
		try {
			return sdf.parse(str);
		} catch (Exception e) {
			return null;
		}
	}

	private List<String> parseJsonArray(String jsonArray) throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(jsonArray, new TypeReference<List<String>>() {
		});

	}

}
