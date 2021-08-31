<script>
  export let className = "";
  export let active = false;
  export let href = "/";
  export let disabled = false;
  export let label;
  export let onClick;
  export let icon;

  const handleLinkClick = e => e.key === " " && e.target.click();

</script>
<style lang="scss">
  .button {
    padding: 10px 20px;
    background-color: var(--color-button-blue);
    border-radius: 8px;
    color: white;
    text-decoration: none;
    cursor: pointer;
    text-align: center;
    font-weight: bold;
    width: 280px;

    @media (max-width: 780px) {
      width: 100%;
    }

    &.icon {
      padding: 2px 16px 2px 16px;
      display: flex;
      align-content: center;
      align-items: center;
    }

    &:hover {
      background-color: var(--color-primary-blue);
      color: whitesmoke;
    }

    :global(svg) {
      width: 62px;
      margin-right: 12px;
    }

  }


</style>

<a class={`button ${className} ${icon ? " icon" : ""}`}
   class:active={active}
   href="{href}"
   class:disabled={disabled}
   on:click|preventDefault|stopPropagation={() => !disabled && onClick()}
   on:keydown={handleLinkClick}>
    {#if icon}
        <span class="html-icon">{@html icon}</span>
    {/if}
    <span>{label}</span>
</a>