
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

*tracking với input là objects:*
Khởi tạo một object MonitorEvent và tiến hành khai báo các thông tin cần tracking ở model này. Ngoài ra còn có các tham số optional để lắng nghe các sự kiện khi tracking, bạn có thể bỏ qua nếu không có nhu cầu.
```css
private fun trackEventByObject() {  
  val monitorEvent = MonitorEvent()  
  monitorEvent.workspaceId = "490bf1f1-2e88-4d6d-8ec4-2bb7de74f9a8"  
  monitorEvent.identityId = hashMapOf(  
  "user_id" to "Object${System.currentTimeMillis()}",  
  "phone" to "0123456789",  
  "email" to "loitp@galaxy.one",  
  "deviceId" to Analytics.getDeviceId(this)  
  )  
  monitorEvent.eventName = "track_now_event"  
  monitorEvent.eventDate = System.currentTimeMillis()  
  monitorEvent.eventData = hashMapOf(  
  "name" to "Loitp",  
  "bod" to "01/01/2000",  
  "player_id" to 123456  
  )  
  Analytics.trackEvent(  
	  monitorEvent = monitorEvent,  
	  onPreExecute = { input ->  
	 },  
	  onResponse = { isSuccessful, code, response ->  
	 },  
	  onFailure = { t ->  
	 }  
  )  
}
```
