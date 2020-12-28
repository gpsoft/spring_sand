function jsConfirmAndSubmit(anchor, action) {
	if ( !confirm(action+'するよ?') ) return;
	anchor.closest('form').submit();
}

function jsChangeLocationInput(radio) {
	let byPref = radio.value == 1;
	document.querySelector('#jsLocationByText').hidden = byPref;
	document.querySelector('#jsLocationBySelect').hidden = !byPref;
}

(()=>{
	jsChangeLocationInput(document.querySelector('input[name="bySelect"]:checked'));
})();