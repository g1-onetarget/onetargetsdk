
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
    api 'com.github.g1mobile:onetargetsdk:1.2.6'
}
```

Sau đó,  sync lại project.


**Cách dùng**

***Init SDK:***
+ Bạn có thể chọn môi trường để tracking là dev hay prod, mặc định sdk sẽ tracking ở môi trường prod.
```css
    private fun setupSDK() {
        val configuration = Configuration(this)
        configuration.setEnvironmentProd()
        configuration.writeKey = "enter your workspace id"
        configuration.isShowLog = false
        configuration.isEnableIAM = true
        configuration.onShowIAM = { htmlContent, iamData ->
            IAM.showIAMActivity(this, htmlContent, iamData)
            //or IAM.showIAMDialog(this, htmlContent, iamData)
        }
        val resultSetupTracking = Analytics.setup(configuration)
        val resultSetupIAM = IAM.setup(configuration, this)
    }
```
Example: https://gitlab.com/g1-data/onetarget-android/-/blob/main/app/src/main/java/com/g1/onetarget/app/G1Application.kt

***Tracking event:***
SDK hỗ trợ tracking bằng 2 phương thức: tracking với input là params và tracking với input là object.

*tracking với input là params:*
Các params workSpaceId, identityId, profile, eventName, eventDate, eventData là các tham số được dùng để tracking. Ngoài ra còn có các tham số optional để lắng nghe các sự kiện khi tracking, bạn có thể bỏ qua nếu không có nhu cầu:
+ onPreExecute: được gọi khi SDK đã  khởi tạo các input thành công và chuẩn bị gọi API tracking.
+ onResponse: được gọi khi SDK đã gọi API xong, trả về cho client biết các thông tin về isSuccessful, code, response.
+ onFailure: khi quá trình tracking có lỗi.

Ví dụ mẫu:
```css
private fun trackEventByParams() {
        val workSpaceId = C.getWorkSpaceId()
        val identityId = hashMapOf<String, Any>(
            "phone" to "0766040293",
            "email" to "loitp@galaxy.one",
        )
        val profile = ArrayList<HashMap<String, Any>>()
        profile.add(
            hashMapOf(
                "profile" to "Loi1",
                "email" to "Loi1@galaxy.one",
            )
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
            profile = profile,
            eventName = eventName,
            eventDate = eventDate,
            eventData = eventData,
            onPreExecute = { input ->
                printBeautyJson(input, tvInput)
                tvOutput?.text = "Loading..."
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
        monitorEvent.workspaceId = C.getWorkSpaceId()
        monitorEvent.identityId = hashMapOf(
            "phone" to "0766040293",
            "email" to "loitp@galaxy.one",
        )
        val profile = ArrayList<HashMap<String, Any>>()
        profile.add(
            hashMapOf(
                "profile" to "Loi1",
                "email" to "Loi1@galaxy.one",
            )
        )
        profile.add(
            hashMapOf(
                "profile" to "Loi2",
                "email" to "Loi2@galaxy.one",
            )
        )
        profile.add(
            hashMapOf(
                "profile" to "Loi3",
                "email" to "Loi3@galaxy.one",
            )
        )
        monitorEvent.profile = profile
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
                printBeautyJson(input, tvInput)
                tvOutput?.text = "Loading..."
            },
            onResponse = { isSuccessful, code, response ->
            },
            onFailure = { t ->
            }
        )
    }
```
