<style global lang="scss">

  .loader:empty,
  .loader:empty:after {
    border-radius: 50%;
    width: 10em;
    height: 10em;
  }

  .loader:empty {
    margin: 60px auto;
    font-size: 10px;
    position: relative;
    text-indent: -9999em;
    border: 1.1em solid white;
    border-top-color: var(--color-primary-blue);
    border-bottom-color: var(--color-primary-blue);
    transform: translateZ(0);
    animation: load8 1.5s infinite linear;
  }

  @-webkit-keyframes load8 {
    0% {
      transform: rotate(0deg);
    }
    100% {
      transform: rotate(360deg);
    }
  }

  @keyframes load8 {
    0% {
      transform: rotate(0deg);
    }
    100% {
      transform: rotate(360deg);
    }
  }
</style>
<div class="loader"></div>
