
# OneTarget Android

**Import SDK**
**Step 1.**   Thêm JitPack repository ở file gradle (level project)
```css
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
**Step 2.**  Thêm dependency SDK ở file gradle (level app)
```css
dependencies {
	        implementation 'com.gitlab.g1-data:onetarget-android:1.0.3'
	}
```

Xem các version release tại [ĐÂY](https://jitpack.io/private#com.gitlab.g1-data/onetarget-android)

Sau đó,  sync lại project.


**Cách dùng**

***Init SDK:***
+ Bạn có thể chọn môi trường để tracking là dev hay prod, mặc định sdk sẽ tracking ở môi trường dev.
```css
    private fun setupTracking() {  
      val configuration = Configuration()  
      configuration.setEnvironmentDev()  
    //configuration.setEnvironmentProd()  
      configuration.writeKey = "490bf1f1-2e88-4d6d-8ec4-2bb7de74f9a8"  
      Analytics.setup(configuration)  
    }
```

***Tracking event:***
SDK hỗ trợ tracking bằng 2 phương thức: tracking với input là params và tracking với input là object.

*tracking với input là params:*
Các params workSpaceId, identityId, eventName, eventDate, eventData là các tham số được dùng để tracking. Ngoài ra còn có các tham số optional để lắng nghe các sự kiện khi tracking, bạn có thể bỏ qua nếu không có nhu cầu:
+ onPreExecute: được gọi khi SDK đã  khởi tạo các input thành công và chuẩn bị gọi API tracking.
+ onResponse: được gọi khi SDK đã gọi API xong, trả về cho client biết các thông tin về isSuccessful, code, response.
+ onFailure: khi quá trình tracking có lỗi.

Ví dụ mẫu:
```css
private fun trackEventByParams() {  
  val workSpaceId = "490bf1f1-2e88-4d6d-8ec4-2bb7de74f9a8"  
  val identityId = hashMapOf<String, Any>(  
  "user_id" to "Params${System.currentTimeMillis()}",  
  "phone" to "0123456789",  
  "email" to "loitp@galaxy.one",  
  "deviceId" to Analytics.getDeviceId(this)  
  )  
  val eventName = "event_name"  
  val eventDate = System.currentTimeMillis()  
  val eventData = hashMapOf<String, Any>(  
  "pageTitle" to "Passenger Information",  
  "pagePath" to "/home"  
  )  
  
  Analytics.trackEvent(  
	  workSpaceId = workSpaceId,  
	  identityId = identityId,  
	  eventName = eventName,  
	  eventDate = eventDate,  
	  eventData = eventData,  
	  onPreExecute = { input ->  
	 },  
	  onResponse = { isSuccessful, code, response ->  
	 },  
	  onFailure = { t ->  
	 }  
	)  
}
```