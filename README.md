# Picsum Photo App

## Functionality
아래 기능들이 포함되 있습니다.
1. `picsum photos api (https://picsum.photos/)`에서 사진 목록을 조회하여 RecyclerView에 노출
2. `Paging api`를 활용하여 자동으로 다음페이지 목록 더보기
3. `RecyclerView`에 특정 이미지 클릭시 이미지 상세화면이 실행되고 이미지 id에 해당하는 `상세 api(https://picsum.photos/id/{id}/info)`를 호출하여 상세정보 노출
4. 상세화면에 좋아요를 클릭하여 `좋아요 여부를 설정`
5. 상세화면에 하단에 `이전과 다음버튼`을 클릭하여 이전 사진이나 다음 사진 상세정보를 조회가능
6. 이미지 목록을 네트웍에서 조회하여 `로컬DB에 저장`하고 네트웍이 끊긴 상태에서도 목록 조회 가능

## Architecture
`MVVM(Model View View Model)` 디자인 패턴을 적용

`Repository Pattern`을 활용하여 데이터 관리 구현

### UI
두개의 화면으로 구성
1. `PhotoListFragment.kt` - 초기 화면이고 이미지 목록을 조회
2. `PhotoDetailFragment.kt` - 이미지의 상세정보를 노출

### Model
`JSON` 데이터를 파싱하여 Kotlin data class 구현

추가로, room database에 저장하기위해 entity class 구현

### ViewModel
`PhotoViewModel.kt`
- 이미지 목록, 상세정보를 조회하고 로컬디비에 저장

### Network
네트웍 계층은 Repository와 ApiService로 구성
1. `PhotoService` - retrofit API를 호출하는 suspend 함수
2. `PhotoRepository` - 네트웍조회와 로컬저장 등을 구현

## Tech Stack
1. [Android appcompat](https://developer.android.com/jetpack/androidx/releases/appcompat), [KTX](https://developer.android.com/kotlin/ktx), [Constraint layout](https://developer.android.com/reference/androidx/constraintlayout/widget/ConstraintLayout), [Material Support](https://material.io/develop/android/docs/getting-started).
2.  [Android Data Binding](https://developer.android.com/topic/libraries/data-binding)
3. [Retrofit](https://square.github.io/retrofit/) for REST API communication.
4. [Coroutine](https://developer.android.com/kotlin/coroutines) for Network call.
5. [Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle), [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
6.  [Livedata](https://developer.android.com/topic/libraries/architecture/livedata)
7. [Room](https://developer.android.com/jetpack/androidx/releases/room) for local database.
8. [Glide](https://github.com/bumptech/glide) for image loading.
9. [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)
10. [Navigation](https://developer.android.com/guide/navigation)
