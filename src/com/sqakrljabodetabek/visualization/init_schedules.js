init();

function init()
{
	setTitle();
	
	for(var i = 0; i < departure_hours.length; i++)
	{
		$('#table_content').append(getTemplateEntrySchedule(departure_hours[i]));
	}
}

function setTitle(title)
{
	var origin = stations[0];
	var destination = stations[stations.length-1]
	$( '#rute' ).html(origin + ' - ' + destination);
}

function getTemplateEntrySchedule(hour)
{
	return "<tr><td>" + hour + "</td></tr>";
}