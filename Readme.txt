


cách thức hoạt động của thuật toán calcMatches:

- List<Set<String>> danh sách chứa các cặp ghép 

 Xem xét danh sách bệnh nhân, và chọn ra bệnh nhân đầu tiên để bắt đầu ghép với bác sĩ - bệnh viện
  - lấy ra danh sách ưu tiên bác sĩ của bệnh nhân 
  - TH1: nếu bác sĩ chưa được ghép :
	+ khởi tạo Set<String> newTriplet , thêm bệnh nhân + bác sĩ vào 
	+ tiến tới giai đoạn 2, ghép bệnh viện: 
		- TH1: nếu bệnh viện chưa được ghép : 
			+ add bệnh viện vào newTriplet, hoàn thành ghép cặp bệnh nhân + bác sĩ + bệnh viện 
			+ add newTriplet vào danh sách 
		- TH2: nếu bệnh viện đã được ghép: 
			+ xem xét mức độ ưu tiên của bệnh viện với cặp triplet trước đó bằng danh sách ưu tiên của bệnh viện với bệnh nhân và với bác sĩ, cụ thể: 
				----> lấy ra thứ tự ưu tiên của bệnh nhân cũ trong ds ưu tiên bệnh nhân + thứ tự ưu tiên của bác sĩ cũ trong ds ưu tiên bác sĩ
				----> đồng thời lấy ra thứ tự ưu tiên của bệnh nhân mới trong ds ưu tiên bệnh nhân + thứ tự ưu tiên của bác sĩ mới trong ds ưu tiên bác sĩ
				===> cặp bệnh nhân - bác sĩ nào được ưu tiên cao hơn thì lấy 
					
					- TH1: Ưu tiên cặp cũ, break, không có gì thay đổi, cặp mới tìm bệnh viện khác để ghép 
					- TH2: ưu tiên cặp mới: + xóa cặp triplet cũ khỏi list, thêm triplet mới vào list 
   - TH2: nếu bác sĩ chưa được ghép, xem xét mức độ ưu tiên của bệnh nhân mới - cũ 
	+ nếu ưu tiên bệnh nhân cũ, break, cặp triplet cũ được giữ lại 
	+ nếu ưu tiên bệnh nhân mới, xóa cặp triplet cũ khỏi list, thêm triplet mới ( lúc này là bệnh nhân - bác sĩ và tiếp tục vòng lặp ghép với bệnh viện )









         
