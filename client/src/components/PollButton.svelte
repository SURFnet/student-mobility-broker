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
        border: 1px solid var(--color-tertiary-grey);
        color: var(--color-tertiary-grey);
        text-decoration: none;
        cursor: pointer;
        text-align: center;


        &:hover {
            background-color: whitesmoke;
        }

        &.active {
            background-color: #7a5c00;
            color: white;
        }
    }


</style>

<button class:active
        on:click={onClick}
        on:keydown={handleLinkClick}>
    {label}
</button>