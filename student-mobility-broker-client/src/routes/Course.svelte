<script>
  import I18n from "i18n-js";
  import {navigate} from "svelte-routing";
  import logoEduid from "../icons/logo_eduID.svg";
  import {onMount} from "svelte";
  import {LottiePlayer} from '@lottiefiles/svelte-lottie-player';
  import scooter from "../lotties/lf20_Fyn2dD.json";
  import {offering} from "../stores/offering";

  const startRegistration = () => {
  }


</script>

<style lang="scss">

</style>
<div>
    {JSON.stringify($offering.offering)}
</div>
<LottiePlayer
        src={scooter}
        autoplay="{true}"
        loop="{true}"
        controls="{false}"
        renderer="svg"
        background="transparent"
        height="{600}"
        width="{600}"
        controlsLayout={null}
/>
