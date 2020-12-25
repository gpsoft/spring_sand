function jsDeleteRiver(anchor) {
	if ( !confirm('削除するよ?') ) return;
	anchor.closest('form').submit();
}
