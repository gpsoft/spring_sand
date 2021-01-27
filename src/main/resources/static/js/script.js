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

function jsWatchValleySorter(sel) {
	if ( sel == null ) return;
	sel.addEventListener('change', ev=>{
		location.href = ev.currentTarget.value;
	});
}

(()=>{
	jsChangeLocationInput(document.querySelector('input[name="bySelect"]:checked'));

	jsWatchValleySorter(document.querySelector('select[name=valley-sorter]'));
})();
