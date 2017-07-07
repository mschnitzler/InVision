package com.kimathi.rfid.invision;

import java.util.ArrayList;

import retrofit2.http.Path;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;

//import com.docmorris.invisionactivityrecorder.model.ActivityRecordDTO;
//@Path("/activity")
public interface ActivityResource {
	



	@PUT("log/{userid}/{activity}")
	public Call<Void> logActivity(@Path("userid") String userid, @Path("activity") long activity);

	//@PUT ("/logs")
	//public Response logActivities(List<ActivityRecordDTO> records);

	/**
	 * @param userid
	 * @return
	 */
	//@GET
	//@Path("/report")
	//@Produces(MediaType.APPLICATION_JSON)
	//public Response getActivityRecords();

	/**
	 * @param userid
	 * @return
	 */
	//@GET
	//@Path("/report/{userid}")
	//Produces(MediaType.APPLICATION_JSON)
	//public Response getActivityRecordsByUser(@PathParam("userid") String userid);

	/**
	 * @return
	 */
	@GET("report/activities")
	public Call<ArrayList<InvisionActivity>> getActivities();

	//@DELETE
	//@Path("/delete/record/{id}")
	//public Response deleteActivityRecordById(@PathParam("id") long id);
	
}
