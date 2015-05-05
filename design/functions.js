draw();

function draw()
{
	setTitle();
	
	for(var i = 0; i < stations.length; i++)
	{
		if(i === 0 || i === stations.length - 1 )
		{
			$('#peta').append(getTemplateStasiunAwalAkhir(stations[i]));
		}
		else
		{
			$('#peta').append(getTemplateStasiunTerminal(stations[i]));
		}
		
		if(i !== stations.length-1)
		{
			$('#peta').append(getTemplateLine(jurusans[i]));
		}
		
	}
}

function setTitle(title)
{
	var origin = stations[0];
	var destination = stations[stations.length-1]
	$( '#rute' ).html(origin + ' - ' + destination);
}

function getTemplateStasiunAwalAkhir(stasiun)
{
	return '<li class="stasiun"><img src="stasiun.png" class="img-indicator" alt=""><div class="dot"></div>' + stasiun + '</li>';
}

function getTemplateStasiunTerminal(stasiun)
{
	return '<li class="stasiun"><img src="transit.png" class="img-indicator" alt=""><div class="dot"></div>' + stasiun + '</li>';
}

function getTemplateLine(line)
{
	return '<li class="jurusan"><div class="line"></div>' + line + '</li>';
}