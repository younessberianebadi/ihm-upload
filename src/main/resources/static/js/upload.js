// When the input tag file is replaced
$('.custom-file-input').on('change',function(){
    $(this).next('.custom-file-label').html($(this)[0].files[0].name);
    // restore submit button
    $('button[type=submit]').prop('disabled',false);
});
// Reselect the file, execute after the reselect button is clicked
$('.reset').click(function(){
    $(this).parent().prev().children('.custom-file-label').html('Click to select...');
    $('.custom-file-input').val('');
    // restore submit button
    $('button[type=submit]').prop('disabled',false);
});
// Execute after the submit button is clicked
$("#uploadFileBtn").click(function(e){

	e.preventDefault();
    // The gray button is invalid, the foreground prevents double submission
    $(this).prop('disabled',true);

    // Get files
    var file = $('#customFile')[0].files[0];
    var formData = new FormData();
    formData.append("file",file);

	 $.ajax({
	      url : '/fileUpload',
	      type : 'POST',
	      data : formData,
	      cache : false,
	      contentType : false,
	      processData : false,
	      xhr: function(){
	        //Get XmlHttpRequest object
	         var xhr = $.ajaxSettings.xhr() ;

	        // Set the onprogress event controller
	         xhr.upload.onprogress = function(event){
	          	var perc = Math.round((event.loaded / event.total) * 100);
	          	$('#progressBar').text(perc + '%');
	          	$('#progressBar').css('width',perc + '%');
	         };
	         return xhr ;
	    	},
	    	beforeSend: function( xhr ) {
	    		// Reset the prompt message to be empty before submitting, and reset the progress bar
	    		$('#alertMsg').text('');
	    		$('#progressBar').text('');
	    		$('#progressBar').css('width','0%');
	              }
	  })
	  .done(function(msg){
		  // Remove the non-display class of the message prompt box
		  $('#alertDiv').addClass("show");
		  // Set the return message
		  $('#alertMsg').text(msg);
		  // Clear the file
	      $('input[type=file]').val('');
	      // restore submit button
	      $('button[type=submit]').prop('disabled',false);
	  })
	  .fail(function(jqXHR){
		  // Remove the non-display class of the message prompt box
		  $('#alertDiv').addClass("show");
		  // Set the return message
		  $('#alertMsg').text("An error occurred");
	  });
	 return false;
});