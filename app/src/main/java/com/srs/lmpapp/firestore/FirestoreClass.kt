package com.srs.lmpapp.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.srs.lmpapp.model.*
import com.srs.lmpapp.ui.activities.*
import com.srs.lmpapp.ui.fragments.*
import com.srs.lmpapp.utils.Constants

class FirestoreClass {
    private val mFireStore=FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User) {

        // The "users" is collection name. If the collection is already created then it will not create the same one again.
        mFireStore.collection("users")
            // Document ID for users fields. Here the document it is the User ID.
            .document(userInfo.id)
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge later on instead of replacing the fields.
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                // Here call a function of base activity for transferring the result to it.
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }


    fun getCurrentUserID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }


    fun getCourseDetails(activity: Activity, courseID: String) {

        // Here we pass the collection name from which we wants the data.
        mFireStore.collection(Constants.TBL_COURSES)
            // The document id to get the Fields of user.
            .document(courseID)
            .get()
            .addOnSuccessListener { document ->

                Log.i(activity.javaClass.simpleName, document.toString())

                // Here we have received the document snapshot which is converted into the User Data model object.
                val course = document.toObject(Course::class.java)!!
                course?.id = courseID
                val sharedPreferences =
                    activity.getSharedPreferences(
                        Constants.MYLMSAPP_PREFERENCES,
                        Context.MODE_PRIVATE
                    )


                when (activity) {
                    is CourseDetailsActivity -> {
                        // Call a function of base activity for transferring the result to it.
                        activity.courseDetailsSuccess(course)

                    }
                    is EnrolledCourseDetailsActivity->
                    {
                        activity.courseDetailsSuccess(course)
                    }

                }
                // END
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error. And print the error in log.
                when (activity) {
                    is CourseDetailsActivity -> {
                        activity.hideProgressDialog()
                    }
                    is EnrolledCourseDetailsActivity->
                    {
                        activity.hideProgressDialog()
                    }

                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )
            }
    }
/*
    fun getSnopshotDetails(fragment:Fragment)
    {
        var totcredits:Int=0
        var totcourses:Int=0
        val currentUserID=getCurrentUserID()
        var user:User?=null
        mFireStore.collection(Constants.TBL_USERS)
                    .document(currentUserID).get().addOnSuccessListener { document ->
                  user = document.toObject(User::class.java)!!
             }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error. And print the error in log.
                when (fragment) {
                    is DashboardFragment -> { fragment.hideProgressDialog()}
                }
                Log.e( fragment.javaClass.simpleName,"Error while getting user details.",e)
            }

        mFireStore.collection(Constants.TBL_COURSES).whereArrayContains("enrolledby",currentUserID).get()
            .addOnSuccessListener { document->
                val courseList: ArrayList<Course> = ArrayList()
                // A for loop as per the list of documents to convert them into Products ArrayList.
                for (i in document.documents) {
                    val course = i.toObject(Course::class.java)
                    course?.id = i.id
                    if (course?.credits!= null) {
                        totcredits = totcredits + course.credits.toInt()
                        totcourses=totcourses+1
                    }
                }
                when (fragment) {
                    is DashboardFragment -> {

                            fragment.getUserDetailsSuccess(user = user!!, totcredits , totcourses)
                                          }

                }
            }
            .addOnFailureListener{e->
                when (fragment) {
                    is DashboardFragment -> {fragment.hideProgressDialog() }
                }
            }
    }
*/
    fun getSnopshotDetails(activity:Activity)
    {
        var totcredits:Int=0
        var totcourses:Int=0
        val currentUserID=getCurrentUserID()
        var user:User?=null
        mFireStore.collection(Constants.TBL_USERS)
            .document(currentUserID).get()
            .addOnSuccessListener { document ->
                user = document.toObject(User::class.java)!!



                if (user?.type.equals("Student", true)) {
                    mFireStore.collection(Constants.TBL_COURSES)
                        .whereArrayContains("enrolledby", currentUserID).get()
                        .addOnSuccessListener { document ->
                            val courseList: ArrayList<Course> = ArrayList()
                            // A for loop as per the list of documents to convert them into Products ArrayList.
                            for (i in document.documents) {
                                val course = i.toObject(Course::class.java)
                                course?.id = i.id
                                if (course?.credits != null) {
                                    totcredits = totcredits + course.credits.toInt()
                                    totcourses = totcourses + 1
                                }
                            }
                            when (activity) {
                                is LoginActivity -> {

                                    activity.userLoggedInSuccess(
                                        user = user!!,
                                        totCredits = totcredits,
                                        totCourses = totcourses
                                    )
                                }

                            }
                        }

                } else {
                    mFireStore.collection(Constants.TBL_COURSES)
                        .whereEqualTo("takenby", currentUserID).get()
                        .addOnSuccessListener { document ->
                            val courseList: ArrayList<Course> = ArrayList()
                            // A for loop as per the list of documents to convert them into Products ArrayList.
                            for (i in document.documents) {
                                val course = i.toObject(Course::class.java)
                                course?.id = i.id
                                if (course?.credits != null) {
                                    totcredits = totcredits + course.credits.toInt()
                                    totcourses = totcourses + 1
                                }
                            }
                            when (activity) {
                                is LoginActivity -> {

                                    activity.userLoggedInSuccess(
                                        user = user!!,
                                        totCredits = totcredits,
                                        totCourses = totcourses
                                    )
                                }

                            }
                        }
                }
            }
                .addOnFailureListener { e ->
                    when (activity) {
                        is LoginActivity -> {
                            activity.hideProgressDialog()
                        }
                    }
                }

    }
    fun getSnopshotDetails(fragment:Fragment)
    {
        var totcredits:Int=0
        var totcourses:Int=0
        val currentUserID=getCurrentUserID()
        var user:User?=null
        mFireStore.collection(Constants.TBL_USERS)
            .document(currentUserID).get()
            .addOnSuccessListener { document ->
                user = document.toObject(User::class.java)!!



                if (user?.type.equals("Student", true)) {
                    mFireStore.collection(Constants.TBL_COURSES)
                        .whereArrayContains("enrolledby", currentUserID).get()
                        .addOnSuccessListener { document ->
                            val courseList: ArrayList<Course> = ArrayList()
                            // A for loop as per the list of documents to convert them into Products ArrayList.
                            for (i in document.documents) {
                                val course = i.toObject(Course::class.java)
                                course?.id = i.id
                                if (course?.credits != null) {
                                    totcredits = totcredits + course.credits.toInt()
                                    totcourses = totcourses + 1
                                }
                            }
                            when (fragment) {
                                is DashboardFragment -> {

                                    fragment.getUserDetailsSuccess(
                                        user = user!!,
                                        totcredits = totcredits,
                                        totcourses = totcourses
                                    )
                                }
                                is FacultyDashboardFragment
                                -> {

                                    fragment.getUserDetailsSuccess(
                                        user = user!!,
                                        totcredits = totcredits,
                                        totcourses = totcourses
                                    )
                                }

                            }
                        }

                } else {
                    mFireStore.collection(Constants.TBL_COURSES)
                        .whereEqualTo("takenby", currentUserID).get()
                        .addOnSuccessListener { document ->
                            val courseList: ArrayList<Course> = ArrayList()
                            // A for loop as per the list of documents to convert them into Products ArrayList.
                            for (i in document.documents) {
                                val course = i.toObject(Course::class.java)
                                course?.id = i.id
                                if (course?.credits != null) {
                                    totcredits = totcredits + course.credits.toInt()
                                    totcourses = totcourses + 1
                                }
                            }
                            when (fragment) {
                                is DashboardFragment -> {

                                    fragment.getUserDetailsSuccess(
                                        user = user!!,
                                        totcredits = totcredits,
                                        totcourses = totcourses
                                    )
                                }
                                is FacultyDashboardFragment -> {

                                    fragment.getUserDetailsSuccess(
                                        user = user!!,
                                        totcredits = totcredits,
                                        totcourses = totcourses
                                    )
                                }

                            }
                        }
                }
            }
            .addOnFailureListener { e ->
                when (fragment) {
                    is DashboardFragment -> {
                        fragment.hideProgressDialog()
                    }
                    is FacultyDashboardFragment -> {
                        fragment.hideProgressDialog()
                    }
                }
            }

    }


    fun getUserDetails(activity: Activity) {

        // Here we pass the collection name from which we wants the data.
        mFireStore.collection(Constants.TBL_USERS)
            // The document id to get the Fields of user.
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                Log.i(activity.javaClass.simpleName, document.toString())

                // Here we have received the document snapshot which is converted into the User Data model object.
                val user = document.toObject(User::class.java)!!
                val sharedPreferences =
                    activity.getSharedPreferences(
                        Constants.MYLMSAPP_PREFERENCES,
                        Context.MODE_PRIVATE
                    )

                // Create an instance of the editor which is help us to edit the SharedPreference.
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    "${user.firstName} ${user.lastName}"
                )
                editor.putString(
                    Constants.LOGGED_IN_USER_EMAIL,
                    "${user.email}"
                )
                editor.putString(
                    Constants.LOGGED_IN_USER_PHONE,
                    " ${user.mobile}"
                )

                editor.putString(
                    Constants.LOGGED_IN_USERID,
                    "${user.id}"
                )
                editor.putString(
                    Constants.LOGGED_IN_USERTYPE,
                    "${user.type}"
                )
                editor.apply()

                when (activity) {
                    is LoginActivity -> {
                        // Call a function of base activity for transferring the result to it.
                        activity.userLoggedInSuccess(user,0,0)
                    }


                }
                // END
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error. And print the error in log.
                when (activity) {
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }

                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )
            }
    }

    fun getUserDetails(fragment: Fragment) {

        // Here we pass the collection name from which we wants the data.
        mFireStore.collection(Constants.TBL_USERS)
            // The document id to get the Fields of user.
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                Log.i(fragment.javaClass.simpleName, document.toString())

                // Here we have received the document snapshot which is converted into the User Data model object.
                val user = document.toObject(User::class.java)!!

                when (fragment) {
                    is DashboardFragment -> {
                        // Call a function of base activity for transferring the result to it.
                        fragment.getUserDetailsSuccess(user,0,0)
                    }

                }
                // END
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error. And print the error in log.
                when (fragment) {
                    is DashboardFragment -> {
                        fragment.hideProgressDialog()
                    }


                }

                Log.e(
                    fragment.javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )
            }
    }


    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>) {
        // Collection Name
        mFireStore.collection(Constants.TBL_USERS)
            // Document ID against which the data to be updated. Here the document id is the current logged in user id.
            .document(getCurrentUserID())
            // A HashMap of fields which are to be updated.
            .update(userHashMap)
            .addOnSuccessListener {

                // Notify the success result.
                when (activity) {
                    is UserProfileActivity -> {
                        // Call a function of base activity for transferring the result to it.
                        activity.userProfileUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener { e ->

                when (activity) {
                    is UserProfileActivity -> {
                        // Hide the progress dialog if there is any error. And print the error in log.
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details.",
                    e
                )
            }
    }

    // A function to upload the image to the cloud storage.
    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?, imageType: String) {

        //getting the storage reference
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            imageType + System.currentTimeMillis() + "."
                    + Constants.getFileExtension(
                activity,
                imageFileURI
            )
        )

        //adding the file to reference
        sRef.putFile(imageFileURI!!)
            .addOnSuccessListener { taskSnapshot ->
                // The image upload is success
                Log.e(
                    "Firebase Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )

                // Get the downloadable url from the task snapshot
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        Log.e("Downloadable Image URL", uri.toString())

                        // Here call a function of base activity for transferring the result to it.
                        when (activity) {
                            is UserProfileActivity -> {
                                activity.imageUploadSuccess(uri.toString())
                            }

                            is AddCourseActivity -> {
                                activity.imageUploadSuccess(uri.toString())
                            }
                        }
                    }
            }
            .addOnFailureListener { exception ->

                // Hide the progress dialog if there is any error. And print the error in log.
                when (activity) {
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                    }

                    is AddCourseActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }

    fun uploadVideoToCloudStorage(activity: Activity, videoFileURI: Uri?, videoType: String) {

        //getting the storage reference
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            videoType + System.currentTimeMillis() + "."
                    + Constants.getFileExtension(
                activity,
                videoFileURI
            )
        )

        //adding the file to reference
        sRef.putFile(videoFileURI!!)
            .addOnSuccessListener { taskSnapshot ->
                // The image upload is success
                Log.e(
                    "Firebase Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )

                // Get the downloadable url from the task snapshot
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        Log.e("Downloadable Image URL", uri.toString())

                        // Here call a function of base activity for transferring the result to it.
                        when (activity) {
                            is AddTopicActivity -> {
                                activity.videoUploadSuccess(uri.toString())
                            }

                        }
                    }
            }
            .addOnFailureListener { exception ->

                // Hide the progress dialog if there is any error. And print the error in log.
                when (activity) {
                    is AddTopicActivity -> {
                        activity.hideProgressDialog()
                    }


                }

                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }

    fun uploadCourseDetails(activity: AddCourseActivity,  courseInfo: Course)
    {

        mFireStore.collection(Constants.TBL_COURSES)
            .document()
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(courseInfo, SetOptions.merge())

            .addOnSuccessListener {

                        activity.courseUploadSuccess()

                     }

                    .addOnFailureListener{ e ->
                        activity.hideProgressDialog()
                        Log.e(activity.javaClass.simpleName,"Error while uploading the courses details.",e)
                    }



    }

    fun getCoursesList(fragment: Fragment,credits:String,category:String) {
        // The collection name for PRODUCTS
        val currentUserID=getCurrentUserID()
        var coursesRef:Query?=null
        when
        {
        credits.isNotEmpty() && category.isNotEmpty() ->coursesRef=mFireStore.collection(Constants.TBL_COURSES)
            .whereEqualTo(Constants.FILTER_COURSE_CREDITS,credits.toInt())
            .whereEqualTo(Constants.FILTER_COURSE_CATEGORY,category)
            credits.isNotEmpty() ->coursesRef=mFireStore.collection(Constants.TBL_COURSES)
                .whereEqualTo(Constants.FILTER_COURSE_CREDITS,credits.toInt())
            category.isNotEmpty()->coursesRef=mFireStore.collection(Constants.TBL_COURSES)
                .whereEqualTo(Constants.FILTER_COURSE_CATEGORY,category)

            else->coursesRef=mFireStore.collection(Constants.TBL_COURSES)


        }

         coursesRef.get() // Will get the documents snapshots.

            .addOnSuccessListener { document ->

                // Here we get the list of boards in the form of documents.
                Log.e("Courses List", document.documents.toString())

                // Here we have created a new instance for Products ArrayList.
                val courseList: ArrayList<Course> = ArrayList()

                // A for loop as per the list of documents to convert them into Products ArrayList.
                for (i in document.documents) {

                    val course = i.toObject(Course::class.java)
                    course?.id=i.id
                    val let = course?.let {

                        if (!course.enrolledby!!.contains(currentUserID))
                        courseList.add(it)

                    }

                }

                when (fragment) {
                    is CourseSearchResultFragment -> {
                        fragment.successCoursesListFromFireStore(courseList)
                    }
                }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error based on the base class instance.
                when (fragment) {
                    is CourseSearchResultFragment -> {
                        fragment.hideProgressDialog()
                    }
                }
                Log.e("Get Product List", "Error while getting product list.", e)
            }
    }
    fun getCourseByName(activity:Activity,coursename:String,startDate:String)
    {
        mFireStore.collection(Constants.TBL_COURSES)

            .whereEqualTo("name", coursename)
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->

                // Here we get the list of boards in the form of documents.
                Log.e("Courses List", document.documents.toString())

                val courseList: ArrayList<Course> = ArrayList()

                for (i in document.documents) {

                    val course:Course ?= i.toObject(Course::class.java)
                    course?.id=i.id
                    course?.let { courseList.add(it) }
                }

                when (activity) {
                    is AddCourseActivity -> {
                        activity.successCoursesDetailsFromFireStore(courseList)
                    }

                }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error based on the base class instance.
                when (activity) {
                    is AddCourseActivity -> {
                        activity.hideProgressDialog()
                    }

                }
                Log.e("Get Product List", "Error while getting product list.", e)
            }

    }
    fun getCoursesListForFaculty(fragment: Fragment) {
        // The collection name for PRODUCTS

        mFireStore.collection(Constants.TBL_COURSES)

            .whereEqualTo(Constants.TAKEN_BY, getCurrentUserID())
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->

                // Here we get the list of boards in the form of documents.
                Log.e("Courses List", document.documents.toString())

                val courseList: ArrayList<Course> = ArrayList()

                for (i in document.documents) {

                    val course:Course ?= i.toObject(Course::class.java)
                    course?.id=i.id
                    course?.let { courseList.add(it) }
                }

                when (fragment) {
                    is FacultyMyCoursesFragment -> {
                        fragment.successCoursesListFromFireStore(courseList)
                    }
                    is FacultyAddCourses->{
                        fragment.successCoursesListFromFireStore(courseList)
                    }
                }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error based on the base class instance.
                when (fragment) {
                    is FacultyMyCoursesFragment -> {
                        fragment.hideProgressDialog()
                    }
                    is FacultyAddCourses -> {
                        fragment.hideProgressDialog()
                    }
                }
                Log.e("Get Product List", "Error while getting product list.", e)
            }
    }


    fun getEnrolledCoursesListForStudent(fragment: Fragment) {

        mFireStore.collection(Constants.TBL_COURSES)

            .whereArrayContains(Constants.ENROLLED_BY, getCurrentUserID())
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->

                // Here we get the list of boards in the form of documents.
                Log.e("Enrolled Courses List", document.documents.toString())

                // Here we have created a new instance for Products ArrayList.
                val enrolledCourseList: ArrayList<Course> = ArrayList()

                // A for loop as per the list of documents to convert them into Products ArrayList.
                for (i in document.documents) {

                    val enrolledcourse = i.toObject(Course::class.java)
                    enrolledcourse?.id=i.id
                    enrolledcourse?.let { enrolledCourseList.add(it) }
                }

                when (fragment) {
                    is MyCoursesFragment -> {
                        fragment.successEnrolledCoursesListFromFireStore(enrolledCourseList)
                    }
                }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error based on the base class instance.
                when (fragment) {
                    is MyCoursesFragment -> {
                        fragment.hideProgressDialog()
                    }
                }
                Log.e("Get Product List", "Error while getting product list.", e)
            }
    }
    fun enrollCourse(activity:CourseDetailsActivity, courseId:String)
    {


        val currentUserId:String=getCurrentUserID()

        var courseDataHashMap=HashMap<String,Any>()
        courseDataHashMap.put("enrolledby", FieldValue.arrayUnion(currentUserId))
        courseDataHashMap.put("remainingseats",FieldValue.increment(-1))

        mFireStore.collection(Constants.TBL_COURSES)
            .document(courseId)
            .update(courseDataHashMap)
            .addOnSuccessListener {

                // Notify the success result.
                when (activity) {
                    is CourseDetailsActivity -> {
                        // Call a function of base activity for transferring the result to it.
                        activity.allDetailsUpdatedSuccessfully()
                    }
                }
            }
            .addOnFailureListener { e ->

                when (activity) {
                    is CourseDetailsActivity -> {
                        // Hide the progress dialog if there is any error. And print the error in log.
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details.",
                    e
                )
            }
    }

    fun unEnrollCourse(activity:EnrolledCourseDetailsActivity, courseId:String)
    {


        val currentUserId:String=getCurrentUserID()

        var courseDataHashMap=HashMap<String,Any>()
        courseDataHashMap.put("enrolledby", FieldValue.arrayRemove(currentUserId))
        courseDataHashMap.put("remainingseats",FieldValue.increment(1))

        mFireStore.collection(Constants.TBL_COURSES)
            .document(courseId)
            .update(courseDataHashMap)
            .addOnSuccessListener {

                // Notify the success result.
                when (activity) {
                    is EnrolledCourseDetailsActivity -> {
                        // Call a function of base activity for transferring the result to it.
                        activity.allDetailsUpdatedSuccessfully()
                    }

                }
            }
            .addOnFailureListener { e ->

                when (activity) {
                    is EnrolledCourseDetailsActivity -> {
                        // Hide the progress dialog if there is any error. And print the error in log.
                        activity.hideProgressDialog()
                    }

                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details.",
                    e
                )
            }
    }
    fun getStudentDetails(activity:Activity, courseId:String)
    {
        val studentList:ArrayList<User> = ArrayList()
        mFireStore.collection(Constants.TBL_COURSES).document(courseId)
            .get()
            .addOnSuccessListener {document ->
                val course = document.toObject(Course::class.java)!!
                course?.id = courseId
                   val enrolledBy:List<String> = course.enrolledby!!

                    if(enrolledBy.size!=0)
                    {

                        mFireStore.collection(Constants.TBL_USERS).whereIn("id",enrolledBy)
                            .get()
                            .addOnSuccessListener { document ->
                                Log.e("Students List", document.documents.toString())


                                for (i in document.documents) {

                                    val student = i.toObject(User::class.java)!!
                                    studentList.add(student)
                                }
                                when(activity)
                                {
                                    is ViewStudentDetailsActivity-> {
                                        activity.successStudentList(studentList)
                                    }

                                }
                            }
                                .addOnFailureListener { e ->
                                    when (activity) {
                                        is ViewStudentDetailsActivity-> {
                                            activity.hideProgressDialog()
                                        }
                                    }

                                    Log.e(activity.javaClass.simpleName,"Error while updating the user details.",e)
                                }
                                }



            }
            .addOnFailureListener { e ->
                when (activity) {
                    is ViewStudentDetailsActivity -> {
                        // Hide the progress dialog if there is any error. And print the error in log.
                        activity.hideProgressDialog()
                    }

                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details.",
                    e
                )
            }
    }
    fun deleteCourse(fragment:FacultyAddCourses,courseID:String)
    {
        mFireStore.collection(Constants.TBL_COURSES)
            .document(courseID)
            .delete()
            .addOnSuccessListener {

                // Notify the success result to the base class.
                fragment.courseDeleteSuccess()
            }
            .addOnFailureListener { e ->

                // Hide the progress dialog if there is an error.
                fragment.hideProgressDialog()

                Log.e(
                    fragment.requireActivity().javaClass.simpleName,
                    "Error while deleting the product.",
                    e
                )
            }
    }
    fun getModuleListFromFireStore(activity:ModulesActivity, moduleIds:List<String>)
    {
        val modules:ArrayList<Module> = ArrayList()

        mFireStore.collection(Constants.TBL_MODULES).get()
            .addOnSuccessListener { document ->
                for (i in document.documents)
                {
                   if(i.id in moduleIds)
                   {
                       val module = i.toObject(Module::class.java)!!
                       module.id=i.id
                       modules.add(module)
                   }
                }
                when (activity) {
                    is ModulesActivity -> {
                        // Hide the progress dialog if there is any error. And print the error in log.
                        activity.successModuleListFromFireStore(modules)
                    }

                }
            }
            .addOnFailureListener { exception ->
                when (activity) {
                    is ModulesActivity -> {
                        // Hide the progress dialog if there is any error. And print the error in log.
                        activity.hideProgressDialog()
                    }

                }
            }
    }
    fun uploadModuleDetails(activity: AddModuleActivity,  moduleInfo: Module)
    {

        mFireStore.collection(Constants.TBL_MODULES).add(moduleInfo)
            .addOnSuccessListener {

                activity.moduleUploadSuccess(it.id)

            }

            .addOnFailureListener{ e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,"Error while uploading the courses details.",e)
            }



    }
    fun uploadTopicDetails(activity: AddTopicActivity,  topicInfo: Topic)
    {
        mFireStore.collection(Constants.TBL_TOPICS).add(topicInfo)
            .addOnSuccessListener {

                activity.topicUploadSuccess(it.id)

            }

            .addOnFailureListener{ e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,"Error while uploading the courses details.",e)
            }

    }

    fun deleteModule(activity : ModulesActivity,moduleId:String)
    {
        mFireStore.collection(Constants.TBL_MODULES)
            .document(moduleId)
            .delete()
            .addOnSuccessListener {

                // Notify the success result to the base class.
                activity.moduleDeleteSuccess()
            }
            .addOnFailureListener { e ->

                // Hide the progress dialog if there is an error.
                activity.hideProgressDialog()

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while deleting the module.",
                    e
                )
            }

    }
    fun deleteTopic(activity:TopicsListActivity,topicId:String)
    {
        mFireStore.collection(Constants.TBL_TOPICS)
            .document(topicId)
            .delete()
            .addOnSuccessListener {

                // Notify the success result to the base class.
                activity.topicDeleteSuccess()
            }
            .addOnFailureListener { e ->

                // Hide the progress dialog if there is an error.
                activity.hideProgressDialog()

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while deleting the module.",
                    e
                )
            }
    }
    fun getTopicListFromFireStore(activity:TopicsListActivity, topicIds:List<String>)
    {
        val topics:ArrayList<Topic> = ArrayList()

        mFireStore.collection(Constants.TBL_TOPICS).get()
            .addOnSuccessListener { document ->
                for (i in document.documents)
                {
                    if(i.id in topicIds)
                    {
                        val topic = i.toObject(Topic::class.java)!!
                        topic.id=i.id
                        topics.add(topic)
                    }
                }
                when (activity) {
                    is TopicsListActivity -> {
                        // Hide the progress dialog if there is any error. And print the error in log.
                        activity.successTopicListFromFireStore(topics)
                    }

                }
            }
            .addOnFailureListener { exception ->
                when (activity) {
                    is TopicsListActivity -> {
                        // Hide the progress dialog if there is any error. And print the error in log.
                        activity.hideProgressDialog()
                    }

                }
            }
    }
    fun addAssignment(activity: AddAssignmentActivity, assignmentInfo: Assignment, courseId:String)

    {
        mFireStore.collection(Constants.TBL_COURSES)
            .document(courseId).collection("assignments").add(assignmentInfo)
            .addOnSuccessListener()
            {
                assignmentInfo.id=it.id
                activity.assignmentAddSuccess(assignmentInfo)
            }
            .addOnFailureListener{ e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,"Error while uploading the Assignment details.",e)
            }

    }
    fun createCourseInstance(activity:CourseDetailsActivity,currentCourse:Course)
    {
        mFireStore.collection(Constants.TBL_COURSES)
            .document(currentCourse.id).collection("students").document(this.getCurrentUserID())
            .set(currentCourse,SetOptions.merge())
            .addOnSuccessListener ()
            {
                Log.d("Assignment","added assignment instance for student: ${getCurrentUserID()}")
                activity.addCourseInstanceSuccessfully()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }


    fun createAssignmentInstance(activity: AddAssignmentActivity,currentAssignment:Assignment,currentCourse:Course)
    {
        var studentList:List<String> = ArrayList()
        studentList= currentCourse.enrolledby!!
        for(studentId in studentList) {
            mFireStore.collection(Constants.TBL_COURSES).document(currentCourse.id)
                .collection("students").document(studentId)
                .collection("assignments").document(currentAssignment.id!!)
                .set(currentAssignment,SetOptions.merge())
                .addOnSuccessListener ()
                        {
                            Log.d("Assignment","added assignment instance for student: ${studentId}")
                         }
                .addOnFailureListener { e ->
                    activity.hideProgressDialog()
                    Log.e(
                        activity.javaClass.simpleName,
                        "Error while registering the user.",
                        e
                    )
                }
        }
        activity.createAssignmentInstanceSuccess()
    }

    fun getCurrentcCourseDetails(activity:CurrentCourseDetailsActivity,courseID:String)
    {
        val assignmentList: ArrayList<Assignment> = ArrayList()
        mFireStore.collection(Constants.TBL_COURSES)
            .document(courseID).collection("students").document(this.getCurrentUserID()).get()
            .addOnSuccessListener { document ->
                val course = document.toObject(Course::class.java)!!
                course?.id = courseID
                mFireStore.collection(Constants.TBL_COURSES)
                    .document(courseID).collection("students").document(this.getCurrentUserID())
                    .collection("assignments")
                    .get().addOnSuccessListener ()
                    {



                    }

                activity.courseDetailsSuccess(course,assignmentList)

            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,"Error while getting user details.", e)

            }


    }
}