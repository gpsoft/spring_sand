function jsConfirmAndSubmit(anchor, action) {
	if ( !confirm(action+'するよ?') ) return;
	anchor.closest('form').submit();
}
