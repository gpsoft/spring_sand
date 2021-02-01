function jsConfirmAndSubmit(anchor, action) {
	if ( !confirm(action+'するよ?') ) return;
	anchor.closest('form').submit();
}

function jsChangedLocationInput(radio) {
	if ( radio == null ) return;
	let isPref = radio.hasAttribute('data-pref-flg');
	jsToggleInput(document.querySelector('#jsLocationByText'), !isPref);
	jsToggleInput(document.querySelector('#jsLocationBySelect'), isPref);
}

function jsToggleInput(inp, on) {
	inp.hidden = !on;
	inp.disabled = !on;
}

function jsWatchValleySorter() {
	let sel = document.querySelector('select[name=valley-sorter]');
	if ( sel == null ) return;

	sel.addEventListener('change', ev=>{
		location.href = ev.currentTarget.value;
	});
}

function jsWatchLoginIdEdit() {
	let inp = document.querySelector('input[name=loginId]');
	if ( inp == null ) return;

	inp.addEventListener('keyup', ev=>{
		document.querySelector('.jsLoginIdAvailable').classList.add('hidden');
		document.querySelector('.jsLoginIdUsed').classList.add('hidden');
		if ( inp.value.length <= 0 ) return;

		let url = document.querySelector('.jsAjaxUrlUniqueLoginId').attributes['href'].value;
		let params = new URLSearchParams('');
		params.append('loginId', inp.value);
		let req = new Request(url+'?'+params.toString());
		window.fetch(req)
			.then(res=>res.json())
			.then(json=>{
				document.querySelector('.jsLoginIdAvailable').classList.toggle('hidden', !json.unique);
				document.querySelector('.jsLoginIdUsed').classList.toggle('hidden', json.unique);
			});
	});
}

(()=>{
	// for lake edit
	jsChangedLocationInput(document.querySelector('input[name="bySelect"]:checked'));

	// for valley index
	jsWatchValleySorter();

	// for user edit
	jsWatchLoginIdEdit();
})();
