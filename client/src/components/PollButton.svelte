<script>
    export let active = false;
    export let label;
    export let onClick;

    const handleLinkClick = e => e.key === " " && e.target.click();

</script>
<style lang="scss">
    button {
        padding: 10px 16px;
        background-color: white;
        border-radius: 6px;
        border: 1px solid #595959;
        color: #595959;;
        text-decoration: none;
        cursor: pointer;
        text-align: center;
        font-size: 14px;

        &:hover {
            background-color: whitesmoke;
            color: black;
        }

        &.active {
            background-color: #7a5c00;
            color: white;

            &:hover {
                background-color: #614c01;
            }
        }
    }


</style>

<button class:active
        on:click={onClick}
        on:keydown={handleLinkClick}>
    {label}
</button>