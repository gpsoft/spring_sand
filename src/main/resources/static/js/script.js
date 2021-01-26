function jsConfirmAndSubmit(anchor, action) {
	if ( !confirm(action+'するよ?') ) return;
	anchor.closest('form').submit();
}

function jsChangeLocationInput(radio) {
	if ( radio == null ) return;
	let isPref = radio.hasAttribute('data-pref-flg');
	jsToggleInput(document.querySelector('#jsLocationByText'), !isPref);
	jsToggleInput(document.querySelector('#jsLocationBySelect'), isPref);
}

function jsToggleInput(inp, on) {
	inp.hidden = !on;
	inp.disabled = !on;
}

(()=>{
	jsChangeLocationInput(document.querySelector('input[name="bySelect"]:checked'));

	document.querySelector('select[name=valley-sorter]')
		.addEventListener('change', ev=>{
			location.href = ev.currentTarget.value;
		});
})();
